package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单表 数据层
 * 
 * @author ruoyi
 */
@Repository
public interface SysFactoryMapper extends JpaRepository<SysFactory,Long>
{
    /**
     * 查询系统所有菜单（含按钮）
     * 
     * @return 菜单列表
     */
    public List<SysFactory> findAll();

    SysFactory findFirstByName(String name);


    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     */
    public void deleteById(Long menuId);

    /**
     * 根据菜单ID查询信息
     * 
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    public SysFactory findFirstById(Long menuId);


}
