package com.p8.common.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author : WX.Y
 * date : 2021/3/11 18:12
 * description :
 */
@Entity
public class UserBean {
    @Id
    private Long id;
    private String name;
    @Transient
    private int tempUsageCount; // not persisted
    @Generated(hash = 2024802960)
    public UserBean(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 1203313951)
    public UserBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

