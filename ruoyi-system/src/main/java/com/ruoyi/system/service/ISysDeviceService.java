package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysDevice;
import com.ruoyi.common.core.domain.entity.SysFactory;

import java.util.List;

/**
 *  业务层
 * 
 * @author ruoyi
 */
public interface ISysDeviceService
{
    /**
     * 根据用户ID查询菜单
     * 
     * @return 菜单列表
     */
    public List<SysDevice> findAllDevices(long factoryId);

    /**
     * 新增保存菜单信息
     * 
     * @return 结果
     */
    public SysDevice saveDevice(SysDevice device);

    public void deleteDevice(Long id);

    /**
     * 修改保存菜单信息
     * 
     * @return 结果
     */
    public SysDevice updateDevice(SysDevice device);

    public SysDevice findById(Long id);

}
