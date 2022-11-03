package com.ball.proxy.service;

import com.ball.base.context.UserContext;
import com.ball.base.model.Const;
import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.TradeConfig;
import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.enums.AllowanceModeEnum;
import com.ball.biz.account.enums.UserLevelEnum;
import com.ball.biz.account.service.IBizAssetAdjustmentOrderService;
import com.ball.biz.account.service.ICurrencyService;
import com.ball.biz.account.service.ITradeConfigService;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.log.enums.OperationBiz;
import com.ball.biz.log.service.IOperationLogService;
import com.ball.biz.user.entity.UserExtend;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserExtendService;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.controller.user.vo.AddUserReq;
import com.ball.proxy.controller.user.vo.UserRefundReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author JimChery
 */
@Slf4j
@Service
public class UserOperationService {
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserAccountService userAccountService;

    @Autowired
    private IBizAssetAdjustmentOrderService assetAdjustmentOrderService;

    @Autowired
    private TransactionSupport transactionSupport;

    @Autowired
    private IOperationLogService operationLogService;

    @Autowired
    private IUserExtendService userExtendService;

    @Autowired
    private ITradeConfigService tradeConfigService;

    @Autowired
    private ICurrencyService currencyService;

    @Value("${trade.buy.min:50}")
    private BigDecimal orderMin;

    public void addUser(AddUserReq req) {
        // 如果当前代理商是代3，则只能添加自己的用户
        Long proxyTmp;
        if (UserTypeEnum.PROXY_THREE.isMe(UserContext.getUserType())) {
            // 添加自己的用户
            proxyTmp = UserContext.getUserNo();
        } else {
            BizAssert.notNull(req.getProxyUid(), BizErrCode.DATA_ERROR);
            // 查询出指定用户
            proxyTmp = req.getProxyUid();
        }
        Long proxy = proxyTmp;
        transactionSupport.execute(() -> {
            // 添加用户
            UserInfo userInfo = userInfoService.addUser(req.getAccount(), req.getUserName(), req.getPassword(), proxy);
            userExtendService.save(new UserExtend().setId(userInfo.getId()).setUserType(UserTypeEnum.GENERAL.v)
                .setHandicapType(req.getHandicapType())
            );
            AllowanceModeEnum modeEnum = AllowanceModeEnum.valueOf(userInfo.getBalanceMode());
            // 添加账户
            userAccountService.init(userInfo.getId(), req.getCurrency(), String.valueOf(UserTypeEnum.GENERAL.v), modeEnum);
            // 调账
            assetAdjustmentOrderService.updateAllowance(userInfo.getId(), req.myAmount(), req.getCurrency(), modeEnum, proxy);
            // 记录日志
            operationLogService.addLog(OperationBiz.ADD_USER, userInfo.getId().toString());
        });
    }

    public void addRefund(@RequestBody @Valid UserRefundReq req) {
        UserInfo userInfo = userInfoService.getByUid(req.getUserId());
        UserExtend userExtend = userExtendService.getByUid(req.getUserId());
        UserLevelEnum userLevelEnum = UserLevelEnum.valueOf(userExtend.getHandicapType());
        // 获取自己的所有上级
        BizAssert.isTrue(Const.hasRelation(userInfo.getProxyInfo(), UserContext.getUserNo()), BizErrCode.DATA_ERROR);
        BigDecimal minAmount = getMin(req.getUserId());
        transactionSupport.execute(() -> {
            req.getConfig().forEach(o -> {
                TradeConfig config = BeanUtil.copy(o, TradeConfig.class);
                switch (userLevelEnum) {
                    case A: config.setA(o.getV());
                        break;
                    case B: config.setB(o.getV());
                        break;
                    case C: config.setC(o.getV());
                        break;
                    case D:
                        default: config.setD(o.getV());
                }
                config.setMin(minAmount);
                config.setUserNo(req.getUserId());
                config.setUserLevel(userExtend.getHandicapType());
                tradeConfigService.init(config, userInfo.getProxyUserId());
            });
            operationLogService.addLog(OperationBiz.ADD_REFUND_CONFIG, req.getUserId().toString());
        });
    }

    public void lock(Long userId) {
        transactionSupport.execute(() -> {
            userInfoService.lock(userId, UserContext.getUserNo());
            operationLogService.addLog(OperationBiz.LOCK_USER, userId.toString());
        });
    }

    public void unlock(Long userId) {
        transactionSupport.execute(() -> {
            userInfoService.unlock(userId, UserContext.getUserNo());
            operationLogService.addLog(OperationBiz.UNLOCK_USER, userId.toString());
        });
    }

    private BigDecimal getMin(Long userId) {
        // 查询用户币种
        UserAccount userAccount = userAccountService.query(userId);
        // 汇率
        BigDecimal rate = currencyService.getRmbRate(userAccount.getCurrency());
        return orderMin.divide(rate, 0, RoundingMode.DOWN);
    }
}
