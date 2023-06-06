package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysFactory;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.shiro.util.AuthorizationUtils;
import com.ruoyi.system.service.ISysFactoryService;
import com.ruoyi.web.controller.tool.JpaUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/system/factory")
public class SysFactoryController extends BaseController {
    private String prefix = "system/factory";

    @Autowired
    private ISysFactoryService factoryService;

    @RequiresPermissions("system:user:view")
    @GetMapping()
    public String user() {
        return prefix + "/factory";
    }

    @RequiresPermissions("system:user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() {
        startPage();
        List<SysFactory> list = factoryService.selectByUser();
        return getDataTable(list);
    }

    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    @RequiresPermissions("system:user:add")
    @Log(title = "厂区管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysFactory factory) {
        factory.setCreateBy(getLoginName());
        return toAjax(factoryService.insertFactory(factory).getId() > 0);
    }

    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public boolean checkLoginNameUnique(SysFactory factory) {
        int i = 0;
        return factoryService.checkMenuNameUnique(factory);
    }

    @RequiresPermissions("system:user:remove")
    @Log(title = "厂区管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {

        factoryService.deleteFactory(Long.parseLong(ids));
        return toAjax(true);
    }

    @RequiresPermissions("system:user:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        mmap.put("factory", factoryService.findById(id));
        return prefix + "/edit";
    }

    @RequiresPermissions("system:user:edit")
    @Log(title = "厂区管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysFactory factory) {
        factory.setUpdateBy(getLoginName());
        factory.setUpdateTime(new Date());
        SysFactory newFactory = factoryService.findById(factory.getId());
        JpaUtil.copyNotNullProperties(factory, newFactory);
        factoryService.updateFactory(newFactory);
        return toAjax(true);
    }

    @RequiresPermissions("system:user:edit")
    @GetMapping("/device/{id}")
    public String toDevicePage(@PathVariable("id") Long id, ModelMap mmap) {
        SysFactory sysFactory = factoryService.findById(id);
        mmap.put("sysFactory", sysFactory);
        return "system/device/device";
    }
}
