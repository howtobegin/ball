package com.ball.biz.base.service.impl;

import com.ball.base.exception.BizErr;
import com.ball.biz.base.entity.IdGen;
import com.ball.biz.base.mapper.IdGenMapper;
import com.ball.biz.base.service.IIdGenService;
import com.ball.biz.base.service.TableNameEnum;
import com.ball.biz.exception.BizErrCode;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 编号生成表 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2022-10-19
 */
@Service
@Slf4j
public class IdGenServiceImpl extends ServiceImpl<IdGenMapper, IdGen> implements IIdGenService {
    @Override
    public long get(TableNameEnum tableName) {
        return get(tableName, 1);
    }

    private long get(TableNameEnum tableName, int count) {
        IdGen idGen = lambdaQuery().eq(IdGen::getTableName, tableName.tableName).one();
        long id = idGen.getSeqNo();
        IdGen update = new IdGen().setSeqNo(id + idGen.getIncr())
                .setVersion(idGen.getVersion() + 1);
        // 执行更新
        boolean flag = lambdaUpdate().eq(IdGen::getTableName, tableName.tableName)
                .eq(IdGen::getVersion, idGen.getVersion())
                .update(update);
        if (!flag) {
            if (count < 20) {
                log.info("表[{}]第[{}]次尝试获取编号", tableName.tableName, count);
                // 支持20次争抢
                return get(tableName, count + 1);
            } else {
                throw new BizErr(BizErrCode.DATA_ERROR);
            }
        }
        return id;
    }
}
