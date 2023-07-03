package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.DayWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单表 数据层
 * 
 * @author ruoyi
 */
@Repository
public interface SysDeviceWorkMapper extends JpaRepository<DayWork,Long>
{


}
