package com.grayash.auditactivity.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PCRuntimeException extends RuntimeException{

    private static final Logger Log = LoggerFactory.getLogger(PCRuntimeException.class);

    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public PCRuntimeException(String customerId){
        super();
        this.customerId = customerId;
        if(Log.isErrorEnabled())
            Log.error("Throwing PCRuntimeException");
    }


}
