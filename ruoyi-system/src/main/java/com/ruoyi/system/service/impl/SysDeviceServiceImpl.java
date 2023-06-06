package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysDevice;
import com.ruoyi.system.mapper.SysDeviceMapper;
import com.ruoyi.system.service.ISysDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SysDeviceServiceImpl implements ISysDeviceService {
    @Autowired
    private SysDeviceMapper deviceMapper;

    @Override
    public List<SysDevice> findAllDevices(long factoryId) {
        return deviceMapper.findAllByFactoryId(factoryId);
    }

    @Override
    public SysDevice saveDevice(SysDevice device) {
        return deviceMapper.save(device);
    }

    @Override
    public void deleteDevice(Long id) {
        deviceMapper.deleteById(id);
    }

    @Override
    public SysDevice updateDevice(SysDevice device) {
        return deviceMapper.save(device);
    }

    @Override
    public SysDevice findById(Long id) {
        return deviceMapper.findFirstById(id);
    }
}
