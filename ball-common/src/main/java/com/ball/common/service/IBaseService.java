package com.ball.common.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ball.base.model.PageResult;
import com.ball.base.model.Paging;
import com.ball.base.util.BeanUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JimChery
 */
public interface IBaseService {
    default  <T> PageResult<T> pageQuery(LambdaQueryChainWrapper wrapper, Paging paging) {
        return pageQuery(wrapper, paging.getPageIndex(), paging.getPageSize());
    }

    default <T> PageResult<T> pageQuery(LambdaQueryChainWrapper wrapper, Paging paging, Class<T> clazz) {
        return pageQuery(wrapper, paging.getPageIndex(), paging.getPageSize(), clazz);
    }

    default <T, V> PageResult<V> pageQuery(LambdaQueryChainWrapper wrapper, int pageIndex, int pageSize, Class<V> clazz) {
        Page<T> page = new Page<>(pageIndex, pageSize);
        wrapper.page(page);
        List<T> records = page.getRecords();
        return new PageResult<>(records.stream().map(o -> BeanUtil.copy(o, clazz)).collect(Collectors.toList()),
                page.getTotal(), pageIndex, pageSize);
    }

    default <T> PageResult<T> pageQuery(LambdaQueryChainWrapper wrapper, int pageIndex, int pageSize) {
        Page<T> page = new Page<>(pageIndex, pageSize);
        wrapper.page(page);
        List<T> records = page.getRecords();
        return new PageResult<>(records, page.getTotal(), pageIndex, pageSize);
    }


}
