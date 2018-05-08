package com.shen.model;

import java.io.Serializable;

public class MyUser implements Serializable {
    private String id;

    private String username;

    private String password;

    private String email;

    private String name;

    private String sex;

    private String birthday;

    private String address;

    private String tel;

    private String qq;

    private String image;

    private String sfjh;

    private String sfzx;

    private String sfhf;

    private String sfpl;

    private String sffx;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getSfjh() {
        return sfjh;
    }

    public void setSfjh(String sfjh) {
        this.sfjh = sfjh == null ? null : sfjh.trim();
    }

    public String getSfzx() {
        return sfzx;
    }

    public void setSfzx(String sfzx) {
        this.sfzx = sfzx == null ? null : sfzx.trim();
    }

    public String getSfhf() {
        return sfhf;
    }

    public void setSfhf(String sfhf) {
        this.sfhf = sfhf == null ? null : sfhf.trim();
    }

    public String getSfpl() {
        return sfpl;
    }

    public void setSfpl(String sfpl) {
        this.sfpl = sfpl == null ? null : sfpl.trim();
    }

    public String getSffx() {
        return sffx;
    }

    public void setSffx(String sffx) {
        this.sffx = sffx == null ? null : sffx.trim();
    }
}