package com.example.ewang.helloworld.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by ewang on 2018/4/21.
 */

public class Message {
    private final long id;

    private final long userId;

    private final long toUserId;

    private final String content;

    private final long sessionId;

    private final Date createTime;

    @JsonCreator
    public Message(@JsonProperty("id") long id,
                   @JsonProperty("userId") long userId,
                   @JsonProperty("toUserId") long toUserId,
                   @JsonProperty("content") String content,
                   @JsonProperty("sessionId") long sessionId,
                   @JsonProperty("createTime") long createTime) {
        this.id = id;
        this.userId = userId;
        this.toUserId = toUserId;
        this.content = content;
        this.sessionId = sessionId;
        this.createTime = new Date(createTime);
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public String getContent() {
        return content;
    }

    public long getSessionId() {
        return sessionId;
    }

    public Date getCreateTime() {
        return createTime;
    }
}
