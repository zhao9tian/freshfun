package com.quxin.freshfun.model.pojo;

import com.quxin.freshfun.model.entity.BaseEntity;

/**
 * Created by ziming on 2016/10/10.
 */
public class NickNamePOJO extends BaseEntity {
    private Long id;   //id
    private String nickName;   //昵称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
