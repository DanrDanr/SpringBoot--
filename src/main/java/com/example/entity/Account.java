package com.example.entity;

import javax.persistence.*;

/**
 * 账户类
 */
public class Account {
    /**
     * @Id 注解表示这个字段是实体的标识符，用来唯一地标识实体对象。
     * @GeneratedValue 注解表示该字段的值是由系统自动生成的
     * strategy = generationtype.identity 表示使用自增长的方式生成主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * @Column属性详解： name
     * 定义了被标注字段在数据库表中所对应字段的名称；
     */
    @Column(name = "name")
    private String name;//名字
    @Column(name = "password")
    private String password;//密码
    @Column(name = "level")
    private Integer level;//等级
    @Column(name = "sex")
    private String sex;//性别
    /**
     * @Transient 表示该属性并非是一个要映射到数据库表中的字段, 只是起辅助作用
     * 用transient关键字标记的成员变量不参与序列化过程
     */
    @Transient
    private String newPassword;
    private String address;//地址
    @Column(name = "nickName")
    private String nickName;//昵称
    private String phone;//号码
    @Transient
    private Double account;//帐户

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }
}
