package com.ball.base.exception;

/**
 * 踢出独有exception
 * @author JimChery
 */
public class KickOutException extends RuntimeException {
    private KickOutException(String message) {
        super(message, null, true, false);
    }

    public static void throwMe(String message) {
        throw new KickOutException(message);
    }

    public static KickOutException instance(String message) {
        return new KickOutException(message);
    }
}
