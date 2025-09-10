package com.trade_app.common.exception;

public class NotFoundError extends RuntimeException{
    public NotFoundError(String message) {
        super(message);
    }

    public NotFoundError() {
        super();
    }
}
