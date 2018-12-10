package me.chanjar.weixin.common.util;

import me.chanjar.weixin.common.api.WxMessageInMemoryDuplicateChecker;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class WxMessageInMemoryDuplicateCheckerTest {

    public void test() throws InterruptedException {
        Long[] msgIds = new Long[]{1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L};
        WxMessageInMemoryDuplicateChecker checker = new WxMessageInMemoryDuplicateChecker(1400L, 1000L);

        // 第一次检查
        for (Long msgId : msgIds) {
            boolean result = checker.isDuplicate(String.valueOf(msgId));
            Assert.assertFalse(result);
        }

        // 过1秒再检查
        Thread.sleep(1000L);
        for (Long msgId : msgIds) {
            boolean result = checker.isDuplicate(String.valueOf(msgId));
            Assert.assertTrue(result);
        }

        // 过1.5秒再检查
        Thread.sleep(1500L);
        for (Long msgId : msgIds) {
            boolean result = checker.isDuplicate(String.valueOf(msgId));
            Assert.assertFalse(result);
        }

    }

}
