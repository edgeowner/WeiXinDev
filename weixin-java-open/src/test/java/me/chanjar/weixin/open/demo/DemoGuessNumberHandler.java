package me.chanjar.weixin.open.demo;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.open.api.WxOpenMessageHandler;
import me.chanjar.weixin.open.api.WxOpenMessageMatcher;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.kefu.WxOpenKefuMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlOutMessage;

import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class DemoGuessNumberHandler implements WxOpenMessageHandler, WxOpenMessageMatcher {

    private Random random = new Random();

    private Pattern pattern = Pattern.compile("\\d+");

    @Override
    public boolean match(WxOpenXmlMessage message) {
        return isUserWantGuess(message) || isUserAnswering(message);
    }

    private boolean isUserWantGuess(WxOpenXmlMessage message) {
        return "猜数字".equals(message.getContent());
    }

    private boolean isUserAnswering(WxOpenXmlMessage message) {
        return this.pattern.matcher(message.getContent()).matches();
    }

    @Override
    public WxOpenXmlOutMessage handle(WxOpenXmlMessage wxMessage, Map<String, Object> context, WxOpenService wxOpenService,
                                      WxSessionManager sessionManager) throws WxErrorException {

        if (isUserWantGuess(wxMessage)) {
            letsGo(wxMessage, wxOpenService, sessionManager);
        }

        if (isUserAnswering(wxMessage)) {
            giveHint(wxMessage, wxOpenService, sessionManager);
        }

        return null;

    }

    protected void letsGo(WxOpenXmlMessage wxMessage, WxOpenService wxOpenService, WxSessionManager sessionManager) throws WxErrorException {
        WxSession session = sessionManager.getSession(wxMessage.getFromUser());
        if (session.getAttribute("guessing") == null) {
            WxOpenKefuMessage m = WxOpenKefuMessage
                    .TEXT()
                    .toUser(wxMessage.getFromUser())
                    .content("请猜一个100以内的数字")
                    .build();
            wxOpenService.getKefuService().sendKefuMessage(m);
        } else {
            WxOpenKefuMessage m = WxOpenKefuMessage
                    .TEXT()
                    .toUser(wxMessage.getFromUser())
                    .content("放弃了吗？那请重新猜一个100以内的数字")
                    .build();
            wxOpenService.getKefuService().sendKefuMessage(m);
        }

        session.setAttribute("guessing", Boolean.TRUE);
        session.setAttribute("number", this.random.nextInt(100));
    }


    protected void giveHint(WxOpenXmlMessage wxMessage, WxOpenService wxOpenService, WxSessionManager sessionManager) throws WxErrorException {

        WxSession session = sessionManager.getSession(wxMessage.getFromUser());

        if (session.getAttribute("guessing") == null) {
            return;
        }
        boolean guessing = (Boolean) session.getAttribute("guessing");
        if (!guessing) {
            return;
        }

        int answer = (Integer) session.getAttribute("number");
        int guessNumber = Integer.valueOf(wxMessage.getContent());
        if (guessNumber < answer) {
            WxOpenKefuMessage m = WxOpenKefuMessage
                    .TEXT()
                    .toUser(wxMessage.getFromUser())
                    .content("小了")
                    .build();
            wxOpenService.getKefuService().sendKefuMessage(m);

        } else if (guessNumber > answer) {
            WxOpenKefuMessage m = WxOpenKefuMessage
                    .TEXT()
                    .toUser(wxMessage.getFromUser())
                    .content("大了")
                    .build();
            wxOpenService.getKefuService().sendKefuMessage(m);
        } else {
            WxOpenKefuMessage m = WxOpenKefuMessage
                    .TEXT()
                    .toUser(wxMessage.getFromUser())
                    .content("Bingo!")
                    .build();
            session.removeAttribute("guessing");
            wxOpenService.getKefuService().sendKefuMessage(m);
        }

    }
}
