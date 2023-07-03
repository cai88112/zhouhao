package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDevice;
import com.ruoyi.common.core.domain.entity.SysFactory;
import com.ruoyi.common.core.domain.entity.WorkData;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.system.mapper.SysDeviceWorkDataMapper;
import com.ruoyi.system.service.ISysDeviceService;
import com.ruoyi.system.service.ISysFactoryService;
import com.ruoyi.web.controller.pojo.WorkDataResponse;
import com.ruoyi.web.controller.pojo.WorkFloor;
import com.ruoyi.web.controller.tool.JpaUtil;
import com.ruoyi.web.controller.tool.MsgService;
import com.ruoyi.web.controller.tool.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/system/device")
public class SysDeviceController extends BaseController {
    private String prefix = "system/device";

    @Autowired
    private ISysDeviceService deviceService;

    @Autowired
    private ISysFactoryService factoryService;

    @Autowired
    private SysDeviceWorkDataMapper deviceWorkDataMapper;

    @RequiresPermissions("system:user:list")
    @PostMapping("/list/{factoryId}")
    @ResponseBody
    public TableDataInfo list(@PathVariable("factoryId") Long factoryId) {
        startPage();
        List<SysDevice> list = deviceService.findAllDevices(factoryId);
        return getDataTable(list);
    }

    @GetMapping("/add/{factoryId}")
    public String add(@PathVariable("factoryId") Long factoryId, ModelMap mmap) {
        SysFactory sysFactory = factoryService.findById(factoryId);
        mmap.put("sysFactory", sysFactory);
        return prefix + "/add";
    }

    @RequiresPermissions("system:user:add")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysDevice device) {
        device.setFactoryId(device.getId());
        device.setId(null);
        device.setCreateBy(getLoginName());
        device.setOnline(false);
        device.setHeartTime(new Date());
        device.setPasswordAuto(false);
        device.setWorkNum(1);
        device.setPwdTime(new Date());
        device.setStatus(false);
        device.setPwd(StringUtil.generateRandomPassword());
        return toAjax(deviceService.saveDevice(device).getId() > 0);
    }


    @RequiresPermissions("system:user:remove")
    @Log(title = "设备管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        deviceService.deleteDevice(Long.parseLong(ids));
        return toAjax(true);
    }

    @RequiresPermissions("system:user:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        mmap.put("device", deviceService.findById(id));
        return prefix + "/edit";
    }

    @RequiresPermissions("system:user:edit")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysDevice device) {
        device.setUpdateBy(getLoginName());
        device.setUpdateTime(new Date());
        SysDevice newDevice = deviceService.findById(device.getId());
        JpaUtil.copyNotNullProperties(device, newDevice);
        deviceService.updateDevice(newDevice);
        return toAjax(true);
    }

    @RequiresPermissions("system:user:edit")
    @GetMapping("/device/{id}")
    public String toDevicePage(@PathVariable("id") Long id, ModelMap mmap) {
        startPage();
        return prefix + "/device";
    }

    @RequiresPermissions("system:user:edit")
    @GetMapping("/detail/{id}")
    public String deviceDetail(@PathVariable("id") Long id, ModelMap mmap) {
        SysDevice device = deviceService.findById(id);
        mmap.put("device",device);
        MsgService.command_32(device.getName(),device.getWorkNum());
        return prefix + "/detail";
    }

    @RequiresPermissions("system:user:add")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping("/setDevice")
    @ResponseBody
    public AjaxResult setDevice(@Validated SysDevice device) {
        device.setUpdateBy(getLoginName());
        device.setUpdateTime(new Date());
        SysDevice newDevice = deviceService.findById(device.getId());
        JpaUtil.copyNotNullProperties(device, newDevice);
        deviceService.updateDevice(newDevice);
        MsgService.command_31(newDevice.getName(),device.isPasswordAuto(), device.isOnline());
        return toAjax(true);
    }
    @RequiresPermissions("system:user:add")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping("/sendCommand")
    @ResponseBody
    public AjaxResult sendCommond(@Validated SysDevice device) {
        SysDevice newDevice = deviceService.findById(device.getId());
        MsgService.command_32(newDevice.getName(),device.getWorkNum());
        return toAjax(true);
    }
    @RequiresPermissions("system:user:add")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping("/getWorkInfo")
    @ResponseBody
    public AjaxResult getWorkInfo(@Validated SysDevice device) {
        WorkData workData = deviceWorkDataMapper.findFirstByDeviceIdAndWorkNum(device.getId(),device.getWorkNum());
        if(workData != null) {
            WorkDataResponse response = MsgService.command_32_parse(workData.getDataHex());
            return new AjaxResult(AjaxResult.Type.SUCCESS, "Success", response);
        }else{
            return new AjaxResult(AjaxResult.Type.ERROR, "Fail", 0);
        }
    }

    @RequiresPermissions("system:user:add")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping("/setParams")
    @ResponseBody
    public AjaxResult setParams(@Validated SysDevice device,WorkDataResponse workDataResponse,String realParams) {
        SysDevice newDevice = deviceService.findById(device.getId());
        try {
            List<WorkFloor> floorList = JSON.parseArray(realParams, WorkFloor.class);
            workDataResponse.setList(floorList);
            workDataResponse.setFloors(floorList.size());
            MsgService.command_33(newDevice.getName(), device.getWorkNum(), workDataResponse);
            return new AjaxResult(AjaxResult.Type.SUCCESS, "Success", 1);
        }catch (Exception e){
            return new AjaxResult(AjaxResult.Type.ERROR, "Fail", 0);
        }
    }
}
