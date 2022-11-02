package com.ball.biz.util;

import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
public class InsertHelper {
    public static  <E> boolean idempotentInsert(IService<E> service, E entity) {
        try {
            service.save(entity);
            return false;
        } catch (Exception e) {
            if (findDuplicateKeyException(e)) {
                log.info("duplicate key for po {}", entity);
                return true;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean findDuplicateKeyException(Throwable e) {
        Throwable rootCause = e;
        while (rootCause.getCause() != null) {
            if (rootCause instanceof DuplicateKeyException
                    || rootCause.getCause() instanceof SQLIntegrityConstraintViolationException) {
                return true;
            }
            rootCause = rootCause.getCause();
        }
        return false;
    }
}
