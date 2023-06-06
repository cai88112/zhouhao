package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.SysFactory;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysFactoryMapper;
import com.ruoyi.system.mapper.SysMenuMapper;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import com.ruoyi.system.service.ISysFactoryService;
import com.ruoyi.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

/**
 * 菜单 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysFactoryServiceImpl implements ISysFactoryService
{

    @Autowired
    private SysFactoryMapper factoryMapper;


    @Override
    public List<SysFactory> selectByUser() {
        return factoryMapper.findAll();
    }


    @Override
    public SysFactory insertFactory(SysFactory menu) {
        return factoryMapper.save(menu);
    }

    @Override
    public void deleteFactory(Long id) {
         factoryMapper.deleteById(id);
    }

    @Override
    public SysFactory updateFactory(SysFactory menu) {
        return factoryMapper.saveAndFlush(menu);
    }

    @Override
    public SysFactory findById(Long id) {
        return factoryMapper.findFirstById(id);
    }

    /**
     * 校验菜单名称是否唯一
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public boolean checkMenuNameUnique(SysFactory menu)
    {
        SysFactory info = factoryMapper.findFirstByName(menu.getName());
        if (StringUtils.isNotNull(info))
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


}
