package com.ball.base.exception;

/**
 * @author JimChery
 */
public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String name) {
        super(name);
    }
}
