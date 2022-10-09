package com.ball.base.exception;

/**
 * @author littlehow
 */
public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String name) {
        super(name);
    }
}
