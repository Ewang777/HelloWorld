package com.example.ewang.helloworld.model;

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

    public Message(long id, long userId, long toUserId, String content, long sessionId, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.toUserId = toUserId;
        this.content = content;
        this.sessionId = sessionId;
        this.createTime = createTime;
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
