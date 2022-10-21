package com.ball.biz.log.service.impl;

import com.ball.base.context.UserContext;
import com.ball.biz.log.entity.OperationLog;
import com.ball.biz.log.enums.OperationBiz;
import com.ball.biz.log.mapper.OperationLogMapper;
import com.ball.biz.log.service.IOperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author atom
 * @since 2022-10-21
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

    @Override
    public void addLog(OperationBiz biz, String bizId, String remark) {
        if (remark != null && remark.length() > 500) {
            remark = remark.substring(0, 500);
        }
        save(new OperationLog()
            .setBizId(bizId).setBizChild(biz.bizChild)
                .setBizType(biz.biz).setOperationAccount(UserContext.getAccount())
                .setOperationUid(UserContext.getUserNo())
                .setRemark(remark)
        );
    }

    @Override
    public void addLog(OperationBiz biz, String bizId) {
        addLog(biz, bizId, null);
    }
}
