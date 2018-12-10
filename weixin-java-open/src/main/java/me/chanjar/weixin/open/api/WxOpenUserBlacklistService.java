package me.chanjar.weixin.open.api;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.open.bean.result.WxOpenUserBlacklistGetResult;

import java.util.List;

/**
 * @author miller
 */
public interface WxOpenUserBlacklistService {
    /**
     * <pre>
     * 获取公众号的黑名单列表
     * 详情请见${@code http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1471422259_pJMWA&token=&lang=zh_CN}
     * </pre>
     */
    WxOpenUserBlacklistGetResult getBlacklist(String nextOpenid) throws WxErrorException;

    /**
     * <pre>
     *   拉黑用户
     *   详情请见 ${@code http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1471422259_pJMWA&token=&lang=zh_CN}
     * </pre>
     */
    void pushToBlacklist(List<String> openidList) throws WxErrorException;

    /**
     * <pre>
     *   取消拉黑用户
     *   详情请见${@code http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1471422259_pJMWA&token=&lang=zh_CN}
     * </pre>
     */
    void pullFromBlacklist(List<String> openidList) throws WxErrorException;
}
