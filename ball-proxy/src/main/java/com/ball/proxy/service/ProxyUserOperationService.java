package com.ball.proxy.service;

import com.ball.base.context.UserContext;
import com.ball.base.transaction.TransactionSupport;
import com.ball.biz.account.enums.AllowanceModeEnum;
import com.ball.biz.account.service.IBizAssetAdjustmentOrderService;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.enums.CurrencyEnum;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.log.enums.OperationBiz;
import com.ball.biz.log.service.IOperationLogService;
import com.ball.biz.user.proxy.ProxyUserService;
import com.ball.proxy.controller.proxy.vo.AddProxyReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
    private ProxyUserService proxyUserService;

    @Autowired
    private TransactionSupport transactionSupport;

    /**
     * 添加代理用户
     * @param req     -
     */
    public void addProxyUser(AddProxyReq req) {
        UserTypeEnum typeEnum = UserTypeEnum.proxyOf(UserContext.getUserType());
        BigDecimal balance = new BigDecimal(req.getAmount());
        AllowanceModeEnum allowanceModeEnum = AllowanceModeEnum.valueOf(req.getBalanceMode());
        transactionSupport.execute(() -> {
            // 添加代理
            Long userId = proxyUserService.addProxy(req.getAccount(), req.getUserName(), req.getPassword(),
                    UserContext.getUserNo(), req.getBalanceMode());
            // 初始化代理账户
            userAccountService.init(userId, CurrencyEnum.RMB.name(), String.valueOf(typeEnum.next), allowanceModeEnum);
            // 调整账务
            assetAdjustmentOrderService.updateAllowance(userId, balance, CurrencyEnum.RMB.name(), allowanceModeEnum, UserContext.getUserNo());
            // 调整上级账务

            // 记录操作日志
            operationLogService.addLog(OperationBiz.ADD_PROXY, userId.toString());
        });
    }

    public void addProxyOne(AddProxyReq req) {
        BigDecimal balance = new BigDecimal(req.getAmount());
        AllowanceModeEnum allowanceModeEnum = AllowanceModeEnum.valueOf(req.getBalanceMode());
        transactionSupport.execute(() -> {
            // 添加代理一
            Long userId = proxyUserService.addProxyOne(req.getAccount(), req.getUserName(), req.getPassword(), req.getBalanceMode());
            // 初始化代理账户
            userAccountService.init(userId, CurrencyEnum.RMB.name(), String.valueOf(UserTypeEnum.PROXY_ONE.v),
                    allowanceModeEnum);
            // 调整账务
            assetAdjustmentOrderService.updateAllowance(userId, balance, CurrencyEnum.RMB.name(), allowanceModeEnum, null);
            // 记录操作日志
            //operationLogService.addLog(OperationBiz.ADD_PROXY, userId.toString(), "添加代理一");
        });
    }
}
