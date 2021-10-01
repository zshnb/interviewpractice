package com.zshnb.interviewpractice.cache_consistency;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "entity")
public class Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键自增
    private Integer id;
    @Column(name = "user_name")
    private String userName;

    public Entity(String userName) {
        this.userName = userName;
    }

    public Entity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
