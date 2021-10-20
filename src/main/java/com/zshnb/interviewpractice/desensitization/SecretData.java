package com.zshnb.interviewpractice.desensitization;

import javax.persistence.*;

@Entity
@Table(name = "secret_data")
public class SecretData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String iv;
    @Column(name = "`key`", nullable = false)
    private String key;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
<<<<<<< HEAD

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
=======
>>>>>>> 586e97c (create encryptUtil)
}
