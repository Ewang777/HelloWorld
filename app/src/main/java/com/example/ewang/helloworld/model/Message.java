package com.example.ewang.helloworld.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ewang on 2018/4/21.
 */

public class Message {

    private final long userId;

    private final long toUserId;

    private final String content;

    private final String username;

    private final String toUsername;

    @JsonCreator
    public Message(@JsonProperty("userId") long userId,
                   @JsonProperty("toUserId") long toUserId,
                   @JsonProperty("content") String content,
                   @JsonProperty("username") String username,
                   @JsonProperty("toUsername") String toUsername) {
        this.userId = userId;
        this.toUserId = toUserId;
        this.content = content;
        this.username = username;
        this.toUsername = toUsername;
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

    public String getUsername() {
        return username;
    }

    public String getToUsername() {
        return toUsername;
    }
}
