package com.ball.boss.service.system;

import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.IDCreator;
import com.ball.boss.context.BossUserContext;
import com.ball.common.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;

import static com.alibaba.fastjson.JSON.toJSONString;

public abstract class BaseService implements IBaseService {
    protected static final String DEPT_ID_DEFAULT = "SYSTEM";
    @Autowired
    protected LogAssist logAssist;

    @Autowired
    protected TransactionSupport transactionSupport;

    /**
     * 获取业务编号
     *
     * @return -
     */
    protected String getBizId() {
        return IDCreator.get();
    }

    /**
     * 获取登录用户编号
     *
     * @return -
     */
    protected String getLoginUserId() {
        return BossUserContext.getUserId();
    }

    /**
     * 获取json字符串
     *
     * @param o -- 目标对象
     * @return
     */
    protected String toJsonString(Object o) {
        if (o == null) {
            return null;
        }
        return toJSONString(o);
    }
}
