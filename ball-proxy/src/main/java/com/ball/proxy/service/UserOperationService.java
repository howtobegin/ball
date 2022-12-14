package com.ball.proxy.service;

import com.ball.base.context.UserContext;
import com.ball.base.model.Const;
import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.TradeConfig;
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
import com.ball.proxy.controller.common.vo.UpdateProxyInfo;
import com.ball.proxy.controller.user.vo.AddUserReq;
import com.ball.proxy.controller.user.vo.UserRefundConfigReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        // ???????????????????????????3?????????????????????????????????
        Long proxyTmp;
        if (UserTypeEnum.PROXY_THREE.isMe(UserContext.getUserType())) {
            // ?????????????????????
            proxyTmp = UserContext.getUserNo();
        } else {
            BizAssert.notNull(req.getProxyUid(), BizErrCode.DATA_ERROR);
            // ?????????????????????
            proxyTmp = req.getProxyUid();
        }
        Long proxy = proxyTmp;
        transactionSupport.execute(() -> {
            // ????????????
            UserInfo userInfo = userInfoService.addUser(req.getAccount(), req.getUserName(), req.getPassword(), proxy);
            userExtendService.save(new UserExtend().setId(userInfo.getId()).setUserType(UserTypeEnum.GENERAL.v)
                .setHandicapType(req.getHandicapType())
            );
            AllowanceModeEnum modeEnum = AllowanceModeEnum.valueOf(userInfo.getBalanceMode());
            // ????????????
            userAccountService.init(userInfo.getId(), req.getCurrency(), String.valueOf(UserTypeEnum.GENERAL.v), modeEnum);
            // ??????
            assetAdjustmentOrderService.updateAllowance(userInfo.getId(), req.myAmount(), req.getCurrency(), modeEnum
                    , proxy, getLogin());
            UserLevelEnum userLevelEnum = UserLevelEnum.valueOf(req.getHandicapType());
            // ?????????????????????
            addRefund(req.getRefund(), userInfo.getId(), userLevelEnum, req.getCurrency(), proxy);
            // ????????????
            operationLogService.addLog(OperationBiz.ADD_USER, userInfo.getId().toString());
        });
    }

    public void addRefund(List<UserRefundConfigReq> refund, Long userId, UserLevelEnum userLevelEnum,
                          String currency, Long proxyUid) {
        BigDecimal minAmount = getMin(currency);
        refund.forEach(o -> {
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
            config.setUserNo(userId);
            config.setUserLevel(userLevelEnum.name());
            tradeConfigService.init(config, proxyUid);
        });
    }

    public void lock(Long userId) {
        UserInfo userInfo = userInfoService.getByUid(userId);
        BizAssert.isTrue(Const.hasRelation(userInfo.getProxyInfo(), UserContext.getUserNo()), BizErrCode.DATA_ERROR);
        transactionSupport.execute(() -> {
            userInfoService.lock(userId, UserContext.getUserNo());
            operationLogService.addLog(OperationBiz.LOCK_USER, userId.toString());
        });
    }

    public void unlock(Long userId) {
        UserInfo userInfo = userInfoService.getByUid(userId);
        BizAssert.isTrue(Const.hasRelation(userInfo.getProxyInfo(), UserContext.getUserNo()), BizErrCode.DATA_ERROR);
        transactionSupport.execute(() -> {
            userInfoService.unlock(userId, UserContext.getUserNo());
            operationLogService.addLog(OperationBiz.UNLOCK_USER, userId.toString());
        });
    }

    public void updatePassword(Long userNo, String password) {
        UserInfo userInfo = userInfoService.getByUid(userNo);
        BizAssert.isTrue(Const.hasRelation(userInfo.getProxyInfo(), UserContext.getUserNo()), BizErrCode.DATA_ERROR);
        transactionSupport.execute(() -> {
            userInfoService.forceChangePassword(userNo, password);
            OperationBiz biz = UserTypeEnum.GENERAL.isMe(userInfo.getUserType())
                    ? OperationBiz.USER_CHANGE_PASSWORD : OperationBiz.PROXY_CHANGE_PASSWORD;

            operationLogService.addLog(biz, userNo.toString());
        });
    }

    public void updateUserInfo(UpdateProxyInfo req) {
        UserInfo userInfo = userInfoService.getByUid(req.getUserNo());
        BizAssert.isTrue(Const.hasRelation(userInfo.getProxyInfo(), UserContext.getUserNo()), BizErrCode.DATA_ERROR);
        transactionSupport.execute(() -> {
            // ????????????????????????????????????
            if (req.hasBalance()) {
                assetAdjustmentOrderService.updateAllowance(userInfo.getId(), req.getBalance(),
                        userInfo.getProxyUserId(), getLogin());
            }
            // ???????????????????????????
            if (req.hasUserName() || req.hasStatus()) {
                userInfoService.lambdaUpdate().set(req.hasUserName(), UserInfo::getUserName, req.getUserName())
                        .set(req.hasStatus(), UserInfo::getStatus, req.getStatus())
                        .eq(UserInfo::getId, req.getUserNo())
                        .update();
            }
            // ?????????????????????
            if (req.hasProxyRate()) {
                UserExtend userExtend = userExtendService.getByUid(userInfo.getProxyUserId());
                BizAssert.isTrue(req.getProxyRate().compareTo(userExtend.getProxyRate()) <= 0, BizErrCode.DATA_ERROR);
                userExtendService.lambdaUpdate().set(UserExtend::getProxyRate, req.getProxyRate())
                        .eq(UserExtend::getId, req.getUserNo())
                        .update();
            }
        });
    }

    private BigDecimal getMin(String currency) {
        // ??????
        BigDecimal rate = currencyService.getRmbRate(currency);
        return orderMin.divide(rate, 0, RoundingMode.DOWN);
    }

    private UserInfo getLogin() {
        return new UserInfo().setId(UserContext.getUserNo())
                .setAccount(UserContext.getAccount())
                .setProxyUserId(UserContext.getProxyUid());
    }
}
