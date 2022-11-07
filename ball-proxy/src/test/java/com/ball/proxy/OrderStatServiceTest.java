package com.ball.proxy;

import com.alibaba.fastjson.JSON;
import com.ball.base.context.AppLoginUser;
import com.ball.base.context.UserContext;
import com.ball.base.model.enums.YesOrNo;
import com.ball.base.util.BizAssert;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.controller.order.vo.stat.SummaryReportReq;
import com.ball.proxy.controller.order.vo.stat.SummaryReportResp;
import com.ball.proxy.service.order.BizOrderStatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author lhl
 * @date 2022/10/17 上午11:36
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BallProxyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderStatServiceTest {
    @Autowired
    private BizOrderStatService bizOrderStatService;
    @Autowired
    private IUserInfoService userInfoService;

    private static final Long USER_NO= 9012448L;

    @Before
    public void init() {
        UserInfo userInfo = userInfoService.getByUid(USER_NO);
        BizAssert.isTrue(YesOrNo.YES.isMe(userInfo.getStatus()), BizErrCode.USER_LOCKED);
        List<UserInfo> proxy = userInfoService.getByProxyInfo(userInfo.getProxyInfo());
        for (UserInfo u : proxy) {
            BizAssert.isTrue(YesOrNo.YES.isMe(u.getStatus()), BizErrCode.USER_LOCKED);
        }
        UserContext.set(new AppLoginUser().setUserNo(USER_NO).setUserName(userInfo.getUserName())
                .setAccount(userInfo.getAccount()).setLoginAccount(userInfo.getLoginAccount())
                .setUserType(userInfo.getUserType()).setProxyInfo(userInfo.getProxyInfo())
                .setProxyUid(userInfo.getProxyUserId())
        );
    }

    @Test
    public void testSummary() {
        SummaryReportReq req = new SummaryReportReq();
        req.setDateType(2);
        SummaryReportResp summary = bizOrderStatService.summary(req);
        log.info("summary {}", JSON.toJSONString(summary));
    }
}
