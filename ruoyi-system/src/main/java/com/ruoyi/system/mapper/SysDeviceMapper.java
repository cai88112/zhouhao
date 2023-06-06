package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysDevice;
import com.ruoyi.common.core.domain.entity.SysFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单表 数据层
 * 
 * @author ruoyi
 */
@Repository
public interface SysDeviceMapper extends JpaRepository<SysDevice,Long>
{
    /**
     * 查询系统所有菜单（含按钮）
     * 
     * @return 菜单列表
     */
    public List<SysDevice> findAllByFactoryId(long id);


    /**
     * 删除菜单管理信息
     *
     */
    public void deleteById(Long id);

    /**
     * 根据菜单ID查询信息
     * 
     * @return 菜单信息
     */
    public SysDevice findFirstById(Long id);


}
