package com.example.ewang.helloworld.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by ewang on 2018/4/20.
 */

public class User {
    private final long id;

    private final String account;

    private final String password;

    private final String username;

    private final Date createTime;

    @JsonCreator
    public User(@JsonProperty("id") long id,
                @JsonProperty("account") String account,
                @JsonProperty("password") String password,
                @JsonProperty("username") String username,
                @JsonProperty("createTime") long createTime) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.username = username;
        this.createTime = new Date(createTime);
    }

    public long getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Date getCreateTime() {
        return createTime;
    }
}
