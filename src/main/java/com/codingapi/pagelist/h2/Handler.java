package com.codingapi.pagelist.h2;

/**
 * @author lorne
 * @date 2019-09-11
 * @description
 */
public abstract class Handler {

    private Handler nextHandler;

    public abstract String request(String type);

    public Handler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
