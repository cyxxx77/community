package com.cyx.community.listener.event;

import org.springframework.context.ApplicationEvent;


public class QuestionRateLimiterEvent extends ApplicationEvent {
    private Long userId;

    public QuestionRateLimiterEvent(Object source) {
        super(source);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public QuestionRateLimiterEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }
}
