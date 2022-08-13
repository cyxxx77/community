package com.cyx.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND("你找的问题不在了，换个试试");

    String message;

    CustomizeErrorCode(String message){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
