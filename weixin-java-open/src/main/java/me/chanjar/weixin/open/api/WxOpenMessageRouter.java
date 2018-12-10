package me.chanjar.weixin.open.api;

import me.chanjar.weixin.common.api.WxErrorExceptionHandler;
import me.chanjar.weixin.common.api.WxMessageDuplicateChecker;
import me.chanjar.weixin.common.api.WxMessageInMemoryDuplicateChecker;
import me.chanjar.weixin.common.session.InternalSession;
import me.chanjar.weixin.common.session.InternalSessionManager;
import me.chanjar.weixin.common.session.StandardSessionManager;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.common.util.LogExceptionHandler;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <pre>
 * 微信消息路由器，通过代码化的配置，把来自微信的消息交给handler处理
 *
 * 说明：
 * 1. 配置路由规则时要按照从细到粗的原则，否则可能消息可能会被提前处理
 * 2. 默认情况下消息只会被处理一次，除非使用 {@link WxOpenMessageRouterRule#next()}
 * 3. 规则的结束必须用{@link WxOpenMessageRouterRule#end()}或者{@link WxOpenMessageRouterRule#next()}，否则不会生效
 *
 * 使用方法：
 * WxOpenMessageRouter router = new WxOpenMessageRouter();
 * router
 *   .rule()
 *       .msgType("MSG_TYPE").event("EVENT").eventKey("EVENT_KEY").content("CONTENT")
 *       .interceptor(interceptor, ...).handler(handler, ...)
 *   .end()
 *   .rule()
 *       // 另外一个匹配规则
 *   .end()
 * ;
 *
 * // 将WxXmlMessage交给消息路由器
 * router.route(message);
 *
 * </pre>
 *
 * @author Daniel Qian
 */
public class WxOpenMessageRouter {

    private static final int DEFAULT_THREAD_POOL_SIZE = 100;
    protected final Logger log = LoggerFactory.getLogger(WxOpenMessageRouter.class);
    private final List<WxOpenMessageRouterRule> rules = new ArrayList<>();

    private final WxOpenService wxOpenService;

    private ExecutorService executorService;

    private WxMessageDuplicateChecker messageDuplicateChecker;

    private WxSessionManager sessionManager;

    private WxErrorExceptionHandler exceptionHandler;

    public WxOpenMessageRouter(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
        this.executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        this.messageDuplicateChecker = new WxMessageInMemoryDuplicateChecker();
        this.sessionManager = new StandardSessionManager();
        this.exceptionHandler = new LogExceptionHandler();
    }

    /**
     * <pre>
     * 设置自定义的 {@link ExecutorService}
     * 如果不调用该方法，默认使用 Executors.newFixedThreadPool(100)
     * </pre>
     *
     * @param executorService
     */
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * <pre>
     * 设置自定义的 {@link me.chanjar.weixin.common.api.WxMessageDuplicateChecker}
     * 如果不调用该方法，默认使用 {@link me.chanjar.weixin.common.api.WxMessageInMemoryDuplicateChecker}
     * </pre>
     *
     * @param messageDuplicateChecker
     */
    public void setMessageDuplicateChecker(WxMessageDuplicateChecker messageDuplicateChecker) {
        this.messageDuplicateChecker = messageDuplicateChecker;
    }

    /**
     * <pre>
     * 设置自定义的{@link me.chanjar.weixin.common.session.WxSessionManager}
     * 如果不调用该方法，默认使用 {@link me.chanjar.weixin.common.session.StandardSessionManager}
     * </pre>
     *
     * @param sessionManager
     */
    public void setSessionManager(WxSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * <pre>
     * 设置自定义的{@link me.chanjar.weixin.common.api.WxErrorExceptionHandler}
     * 如果不调用该方法，默认使用 {@link me.chanjar.weixin.common.util.LogExceptionHandler}
     * </pre>
     *
     * @param exceptionHandler
     */
    public void setExceptionHandler(WxErrorExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    List<WxOpenMessageRouterRule> getRules() {
        return this.rules;
    }

    /**
     * 开始一个新的Route规则
     */
    public WxOpenMessageRouterRule rule() {
        return new WxOpenMessageRouterRule(this);
    }

    /**
     * 处理微信消息
     *
     * @param wxMessage
     */
    public WxOpenXmlOutMessage route(final WxOpenXmlMessage wxMessage) {
        if (isDuplicateMessage(wxMessage)) {
            // 如果是重复消息，那么就不做处理
            return null;
        }

        final List<WxOpenMessageRouterRule> matchRules = new ArrayList<>();
        // 收集匹配的规则
        for (final WxOpenMessageRouterRule rule : this.rules) {
            if (rule.test(wxMessage)) {
                matchRules.add(rule);
                if (!rule.isReEnter()) {
                    break;
                }
            }
        }

        if (matchRules.size() == 0) {
            return null;
        }

        WxOpenXmlOutMessage res = null;
        final List<Future<?>> futures = new ArrayList<>();
        for (final WxOpenMessageRouterRule rule : matchRules) {
            // 返回最后一个非异步的rule的执行结果
            if (rule.isAsync()) {
                futures.add(
                        this.executorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                rule.service(wxMessage, WxOpenMessageRouter.this.wxOpenService, WxOpenMessageRouter.this.sessionManager, WxOpenMessageRouter.this.exceptionHandler);
                            }
                        })
                );
            } else {
                res = rule.service(wxMessage, this.wxOpenService, this.sessionManager, this.exceptionHandler);
                // 在同步操作结束，session访问结束
                this.log.debug("End session access: async=false, sessionId={}", wxMessage.getFromUser());
                sessionEndAccess(wxMessage);
            }
        }

        if (futures.size() > 0) {
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (Future<?> future : futures) {
                        try {
                            future.get();
                            WxOpenMessageRouter.this.log.debug("End session access: async=true, sessionId={}", wxMessage.getFromUser());
                            // 异步操作结束，session访问结束
                            sessionEndAccess(wxMessage);
                        } catch (InterruptedException e) {
                            WxOpenMessageRouter.this.log.error("Error happened when wait task finish", e);
                        } catch (ExecutionException e) {
                            WxOpenMessageRouter.this.log.error("Error happened when wait task finish", e);
                        }
                    }
                }
            });
        }
        return res;
    }

    protected boolean isDuplicateMessage(WxOpenXmlMessage wxMessage) {

        StringBuffer messageId = new StringBuffer();
        if (wxMessage.getMsgId() == null) {
            messageId.append(wxMessage.getCreateTime())
                    .append("-").append(wxMessage.getFromUser())
                    .append("-").append(wxMessage.getEventKey() == null ? "" : wxMessage.getEventKey())
                    .append("-").append(wxMessage.getEvent() == null ? "" : wxMessage.getEvent())
            ;
        } else {
            messageId.append(wxMessage.getMsgId());
        }

        return this.messageDuplicateChecker.isDuplicate(messageId.toString());

    }

    /**
     * 对session的访问结束
     *
     * @param wxMessage
     */
    protected void sessionEndAccess(WxOpenXmlMessage wxMessage) {

        InternalSession session = ((InternalSessionManager) this.sessionManager).findSession(wxMessage.getFromUser());
        if (session != null) {
            session.endAccess();
        }

    }
}