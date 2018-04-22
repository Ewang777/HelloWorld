package com.example.ewang.helloworld.helper;

/**
 * Created by ewang on 2018/4/22.
 */

public class ResponseWrapper {
    private final boolean success;

    private final String errMessage;

    private final Object returnVal;

    private final String returnType;

    public ResponseWrapper(boolean success, String errMessage, Object returnVal, String returnType) {
        this.success = success;
        this.errMessage = errMessage;
        this.returnVal = returnVal;
        this.returnType = returnType;
    }

    public ResponseWrapper(String errMessage) {
        this.success = false;
        this.errMessage = errMessage;
        this.returnVal = null;
        this.returnType = null;
    }

    public ResponseWrapper(Object returnVal, String returnType) {
        this.success = true;
        this.errMessage = null;
        this.returnVal = returnVal;
        this.returnType = returnType;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public Object getReturnVal() {
        return returnVal;
    }

    public String getReturnType() {
        return returnType;
    }
}
