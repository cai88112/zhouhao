package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.WorkData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 菜单表 数据层
 * 
 * @author ruoyi
 */
@Repository
public interface SysDeviceWorkDataMapper extends JpaRepository<WorkData,Long>
{

    WorkData findFirstByDeviceIdAndWorkNum(Long deviceId,Integer workNum);

}
