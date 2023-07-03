package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseJPAEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * factory sys_factory
 * 
 * @author ruoyi
 */
@Entity
@Table(name = "sys_device_work_data")
@DynamicUpdate
public class WorkData extends BaseJPAEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 名称 */
    @Column(name = "data_hex")
    private String dataHex;

    @Column(name = "work_num")
    private Integer workNum;

    @Column(name = "device_id")
    private Long deviceId;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataHex() {
        return dataHex;
    }

    public void setDataHex(String dataHex) {
        this.dataHex = dataHex;
    }

    public Integer getWorkNum() {
        return workNum;
    }

    public void setWorkNum(Integer workNum) {
        this.workNum = workNum;
    }
}
