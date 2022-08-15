package com.cyx.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND("你找的问题不在了，换个试试",2001),
    TARGET_PARAM_NOT_FOUND("未选中任何问题或评论进行回复",2002),
    NO_LOGIN("未登录无法评论",2003),
    SYS_ERROR("服务器冒烟了",2004),
    TYPE_PARAM_WRONG("评论类型错误或不存在",2005),
    COMMENT_NOT_FOUND("回复的评论不存在",2006),
    CONTENT_IS_EMPTY("输入内容不能为空",2007),
    READ_NOTIFICATION_FAIL("信息读取错误",2008),
    NOTIFICATION_NOT_FOUND("通知不存在",2009),
    FILE_UPLOAD_FAIL("文件上传失败",2010),
    INVALID_INPUT( "非法输入",2011),
    INVALID_OPERATION("兄弟，是不是走错房间了？",2012),
    USER_DISABLE( "操作被禁用，如有疑问请联系管理员",2013),
    RATE_LIMIT( "操作太快了，请稍后重试",2014)
    ;
    private String message;
    private Integer code;


    CustomizeErrorCode(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
