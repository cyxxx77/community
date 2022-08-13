package com.cyx.community.exception;


public class CustomizeException extends RuntimeException{


    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message = errorCode.getMessage();
    }

    public CustomizeException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
