package com.example.ewang.helloworld.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by ewang on 2018/5/10.
 */

public class Session {
    private final long id;

    private final long userId;

    private final long toUserId;

    private final Date createTime;

    private final Date updateTime;

    private final int unread;

    @JsonCreator
    public Session(@JsonProperty("id") long id,
                   @JsonProperty("userId") long userId,
                   @JsonProperty("toUserId") long toUserId,
                   @JsonProperty("createTime") long createTime,
                   @JsonProperty("updateTime") long updateTime,
                   @JsonProperty("unread") int unread) {
        this.id = id;
        this.userId = userId;
        this.toUserId = toUserId;
        this.createTime = new Date(createTime);
        this.updateTime = new Date(updateTime);
        this.unread = unread;
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

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public int getUnread() {
        return unread;
    }
}
