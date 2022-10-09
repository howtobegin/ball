package com.ball.base.transaction;

public interface TransactionExecuteWithResult<T> {
    T run();
}
