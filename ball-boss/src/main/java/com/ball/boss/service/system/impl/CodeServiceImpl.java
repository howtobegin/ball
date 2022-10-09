package com.ball.boss.service.system.impl;


import com.ball.base.model.PageResult;
import com.ball.base.util.BizAssert;
import com.ball.boss.dao.entity.BossCode;
import com.ball.boss.exception.BossErrorCode;
import com.ball.boss.service.IBossCodeService;
import com.ball.boss.service.system.BaseService;
import com.ball.boss.service.system.CodeService;
import com.ball.boss.service.system.model.EditMode;
import com.ball.boss.service.system.model.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CodeServiceImpl extends BaseService implements CodeService {
    private static final String OPERATION_BIZ = "code";

    @Autowired
    private IBossCodeService codeService;

    @Override
    public String add(BossCode code) {
        code.setCodeId(getBizId());
        log.info("CodeService add code={}", toJsonString(code));
        transactionSupport.execute(() -> {
            codeService.save(code);
            logAssist.operationLog(OperationType.ADD, OPERATION_BIZ, code.getCodeId());
        });

        return code.getCodeId();
    }

    @Override
    public void update(BossCode code) {
        log.info("CodeService update code={}", code);
        BossCode dbCode = codeService.lambdaQuery().eq(BossCode::getCodeId, code.getCodeId()).one();
        BizAssert.notNull(dbCode, BossErrorCode.DATA_NOT_EXISTS);
        BizAssert.isTrue(!EditMode.isReadOnly(dbCode.getEditMode()), BossErrorCode.DATA_NOT_EDITABLE);
        code.setId(dbCode.getId());
        transactionSupport.execute(() -> {
            codeService.updateById(code);
            logAssist.operationLog(OperationType.UPDATE, OPERATION_BIZ, code.getCodeId());
        });

    }

    @Override
    public void delete(String codeId) {
        log.info("CodeService delete codeId={}", codeId);
        BossCode dbCode = codeService.lambdaQuery().eq(BossCode::getCodeId, codeId).one();
        BizAssert.notNull(dbCode, BossErrorCode.DATA_NOT_EXISTS);
        BizAssert.isTrue(!EditMode.isReadOnly(dbCode.getEditMode()), BossErrorCode.DATA_NOT_EDITABLE);
        transactionSupport.execute(() -> {
            codeService.lambdaUpdate().eq(BossCode::getCodeId, codeId).remove();
            logAssist.operationLog(OperationType.DELETE, OPERATION_BIZ, codeId);
        });

    }

    @Override
    public BossCode queryById(String codeId) {
        log.info("CodeService queryById codeId={}", codeId);
        BossCode code = codeService.lambdaQuery().eq(BossCode::getCodeId, codeId).one();
        BizAssert.notNull(code, BossErrorCode.DATA_NOT_EXISTS);
        return code;
    }

    @Override
    public List<BossCode> queryByField(String field) {
        log.info("CodeService queryByField field={}", field);
        return codeService.lambdaQuery().eq(BossCode::getField, field).list();
    }

    @Override
    public List<BossCode> queryAll() {
        log.info("CodeService queryAll");
        return codeService.lambdaQuery().orderByAsc(BossCode::getSortNo).list();
    }

    @Override
    public PageResult<BossCode> queryPaging(BossCode code) {
        log.info("CodeService queryPaging code={}", toJsonString(code));
        return pageQuery(codeService.lambdaQuery()
                .eq(code.getField() != null, BossCode::getField, code.getField())
                .orderByAsc(BossCode::getSortNo), code);
    }
}
