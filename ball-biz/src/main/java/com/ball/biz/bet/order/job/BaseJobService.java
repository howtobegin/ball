package com.ball.biz.bet.order.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @author scliuhailin@163.com
 * @date 2022/10/20 上午10:42
 */
@Slf4j
public abstract class BaseJobService<T> {
    @Value("${order.base.job.max.loop.times:20}")
    private int maxLoopTimes;

    private volatile Long maxCallbackId = 0L;

    public void execute() {
        log.info("[{}]pageSize {} maxLoopTimes {}", getClass().getSimpleName(), getPageSize(), maxLoopTimes);
        boolean midOrderError = false;
        for (int i = 0; i < maxLoopTimes; i++) {
            List<T> datas = fetchData();
            if (CollectionUtils.isEmpty(datas)) {
                log.info("[{}] there is no data call", getClass().getSimpleName());
                break;
            }
            boolean allOrderError = true;
            for (T data : datas) {
                boolean doResult = executeOne(data);
                if (doResult) {
                    maxCallbackId = !midOrderError ? getId(data) : maxCallbackId;
                    allOrderError = false;
                } else {
                    midOrderError = true;
                }
            }
            if (allOrderError) {
                log.info("[{}] exception occurs for more than {} datas, please modify config's value [xx.xx.job.page.size]", getClass().getSimpleName(), getPageSize());
                return;
            }
        }
    }

    public abstract boolean executeOne(T data);

    public abstract List<T> fetchData();

    public abstract Long getId(T data);

    public abstract int getPageSize();
}
