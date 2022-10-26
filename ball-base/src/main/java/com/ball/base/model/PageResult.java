package com.ball.base.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author JimChery
 */

@Getter
@Setter
public class PageResult<T> {
    private List<T> rows;
    //总条数
    private long totalNum;
    //页大小
    private int pageSize;
    //当前第几页
    private int pageIndex;

    public PageResult(List<T> rows, long totalNum, int pageIndex, int pageSize) {
        this.rows = rows;
        this.totalNum = totalNum;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    /**
     * 得到 总页数
     *
     * @return
     */
    public int getTotalPages() {
        if (totalNum <= 0) {
            return 0;
        }
        if (pageSize <= 0) {
            return 0;
        }

        int count = (int) (totalNum / pageSize);
        if (totalNum % pageSize > 0) {
            count++;
        }
        return count;
    }

    public boolean hasResult() {
        return !CollectionUtils.isEmpty(rows);
    }

    public void foreach(Consumer<T> action) {
        rows.forEach(action);
    }

    public Stream<T> stream() {
        return rows.stream();
    }

    public void add(T t) {
        rows.add(t);
    }
}
