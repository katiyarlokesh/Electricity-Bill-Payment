package com.ebp.exceptionHandler;

/**
 * @Author rohit.parihar 9/17/2022
 * @Class authorizationException
 * @Project Electricity Bill Payment
 */

public class authorizationException extends RuntimeException {
    public authorizationException(String message) {
        super(message);
    }

    public authorizationException() {
    }
}
