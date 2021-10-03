package com.zshnb.interviewpractice.cache_consistency;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "cache_info")
public class CacheInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键自增
    private Integer id;
    @Column
    private String name;
    @Column
    private Boolean valid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
