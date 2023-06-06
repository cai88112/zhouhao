package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.SysFactory;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  业务层
 * 
 * @author ruoyi
 */
public interface ISysFactoryService
{
    /**
     * 根据用户ID查询菜单
     * 
     * @return 菜单列表
     */
    public List<SysFactory> selectByUser();

    /**
     * 新增保存菜单信息
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    public SysFactory insertFactory(SysFactory menu);

    public void deleteFactory(Long id);

    /**
     * 修改保存菜单信息
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    public SysFactory updateFactory(SysFactory menu);

    public SysFactory findById(Long id);

    /**
     * 校验菜单名称是否唯一
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean checkMenuNameUnique(SysFactory menu);
}
