package com.ball.proxy.service;

import com.ball.base.context.UserContext;
import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.TradeConfig;
import com.ball.biz.account.enums.AllowanceModeEnum;
import com.ball.biz.account.service.IBizAssetAdjustmentOrderService;
import com.ball.biz.account.service.ITradeConfigService;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.enums.CurrencyEnum;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.log.enums.OperationBiz;
import com.ball.biz.log.service.IOperationLogService;
import com.ball.biz.user.entity.UserExtend;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.proxy.ProxyUserService;
import com.ball.biz.user.service.IUserExtendService;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.controller.proxy.vo.AddProxyReq;
import com.ball.proxy.controller.proxy.vo.ProxyRefundConfigReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author JimChery
 */
@Service
@Slf4j
public class ProxyUserOperationService {

    @Autowired
    private IOperationLogService operationLogService;

    @Autowired
    private IUserAccountService userAccountService;

    @Autowired
    private IBizAssetAdjustmentOrderService assetAdjustmentOrderService;

    @Autowired
    private IUserExtendService userExtendService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ProxyUserService proxyUserService;

    @Autowired
    private TransactionSupport transactionSupport;

    @Autowired
    private ITradeConfigService tradeConfigService;

    @Value("${trade.buy.min:50}")
    private BigDecimal orderMin;

    /**
     * 添加代理用户
     * @param req     -
     */
    public void addProxyUser(AddProxyReq req) {
        UserTypeEnum typeEnum = UserTypeEnum.proxyOf(UserContext.getUserType());
        BigDecimal balance = new BigDecimal(req.getAmount());
        AllowanceModeEnum allowanceModeEnum = AllowanceModeEnum.valueOf(req.getBalanceMode());
        UserExtend userExtend = userExtendService.getByUid(UserContext.getUserNo());
        BizAssert.isTrue(req.getProxyRate().compareTo(userExtend.getProxyRate()) <= 0 &&
                req.getProxyRate().compareTo(BigDecimal.ZERO) >= 0, BizErrCode.PARAM_ERROR_DESC, "proxyRate");
        transactionSupport.execute(() -> {
            // 添加代理
            Long userId = proxyUserService.addProxy(req.getAccount(), req.getUserName(), req.getPassword(),
                    UserContext.getUserNo(), req.getBalanceMode());
            // 添加扩展属性
            userExtendService.save(new UserExtend().setId(userId).setProxyRate(req.getProxyRate())
                    .setTotalProxyRate(userExtend.getProxyRate()).setUserType(typeEnum.next));
            // 初始化代理账户
            userAccountService.init(userId, CurrencyEnum.RMB.name(), String.valueOf(typeEnum.next), allowanceModeEnum);
            // 调整账务
            assetAdjustmentOrderService.updateAllowance(userId, balance, CurrencyEnum.RMB.name(), allowanceModeEnum,
                    UserContext.getUserNo(), getLogin());
            // 添加退水和限额
            addRefundConfig(req.getRefund(), userId);
            // 记录操作日志
            operationLogService.addLog(OperationBiz.ADD_PROXY, userId.toString());
        });
    }

    public void addProxyOne(AddProxyReq req) {
        BigDecimal balance = new BigDecimal(req.getAmount());
        AllowanceModeEnum allowanceModeEnum = AllowanceModeEnum.valueOf(req.getBalanceMode());
        // 代理商分成比例不可大于1
        BizAssert.isTrue(req.getProxyRate().compareTo(BigDecimal.ONE) <= 0 &&
                req.getProxyRate().compareTo(BigDecimal.ZERO) >= 0, BizErrCode.PARAM_ERROR_DESC, "proxyRate");
        // 代理一的总代默认1
        transactionSupport.execute(() -> {
            // 添加代理一
            Long userId = proxyUserService.addProxyOne(req.getAccount(), req.getUserName(), req.getPassword(), req.getBalanceMode());
            // 设置扩展属性
            userExtendService.save(new UserExtend().setId(userId).setProxyRate(req.getProxyRate())
                    .setTotalProxyRate(BigDecimal.ONE).setUserType(UserTypeEnum.PROXY_ONE.v));
            // 初始化代理账户
            userAccountService.init(userId, CurrencyEnum.RMB.name(), String.valueOf(UserTypeEnum.PROXY_ONE.v),
                    allowanceModeEnum);
            // 调整账务
            assetAdjustmentOrderService.updateAllowance(userId, balance, CurrencyEnum.RMB.name(), allowanceModeEnum,
                    null, getLogin());
            // 添加退水和限额
            addRefundConfig(req.getRefund(), userId);
            // 记录操作日志
            //operationLogService.addLog(OperationBiz.ADD_PROXY, userId.toString(), "添加代理一");
        });
    }

    public void addRefundConfig(List<ProxyRefundConfigReq> refund, Long userId) {
        refund.forEach(o -> {
            TradeConfig config = BeanUtil.copy(o, TradeConfig.class);
            config.setUserNo(userId);
            if (config.getMin() == null) {
                config.setMin(orderMin);
            }
            tradeConfigService.init(config, UserContext.getUserNo());
        });
    }

    public void updatePassword(Long userNo, String oldPassword, String password) {
        transactionSupport.execute(() -> {
            userInfoService.changePassword(userNo, oldPassword, password);
            operationLogService.addLog(OperationBiz.PROXY_CHANGE_PASSWORD, userNo.toString());
        });
    }

    public void updatePasswordFirst(Long userNo, String password) {
        transactionSupport.execute(() -> {
            userInfoService.firstChangePassword(userNo, password);
            operationLogService.addLog(OperationBiz.PROXY_CHANGE_PASSWORD, userNo.toString());
        });
    }

    private UserInfo getLogin() {
        return new UserInfo().setId(UserContext.getUserNo())
                .setAccount(UserContext.getAccount())
                .setProxyUserId(UserContext.getProxyUid());
    }
}
