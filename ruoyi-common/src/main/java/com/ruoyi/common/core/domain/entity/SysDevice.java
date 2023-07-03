package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseJPAEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * factory sys_factory
 * 
 * @author ruoyi
 */
@Entity
@Table(name = "sys_device")
@DynamicUpdate
public class SysDevice extends BaseJPAEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 名称 */
    @Column(name = "name")
    private String name;

    @Column(name = "alias")
    private String alias;

    @Column(name = "pwd")
    private String pwd;

    @Column(name = "heart_time")
    private Date heartTime;

    @Column(name = "pwd_time")
    private Date pwdTime;

    public Date getPwdTime() {
        return pwdTime;
    }

    public void setPwdTime(Date pwdTime) {
        this.pwdTime = pwdTime;
    }

    @Column(name = "work_num")
    private Integer workNum;

    @Column(name = "password_auto")
    private boolean passwordAuto;

    @Column(name = "status")
    private boolean status;

    @Column(name = "factory_id", nullable = false)
    private Long factoryId;

    @Column(name = "online", nullable = false)
    private boolean online;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Date getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(Date heartTime) {
        this.heartTime = heartTime;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getWorkNum() {
        return workNum;
    }

    public void setWorkNum(Integer workNum) {
        this.workNum = workNum;
    }

    public boolean isPasswordAuto() {
        return passwordAuto;
    }

    public void setPasswordAuto(boolean passwordAuto) {
        this.passwordAuto = passwordAuto;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

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
}
