package com.github.judo.admin.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.github.judo.admin.model.dto.RoleDTO;
import com.github.judo.admin.model.entity.SysRole;
import com.github.judo.common.util.Query;

import java.util.List;

/**
 * @Auther: xiangjunzhong@qq.com
 * @Description: 服务类
 * @Version: 1.0
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 添加角色
     *
     * @param roleDto 角色信息
     * @return 成功、失败
     */
    Boolean insertRole(RoleDTO roleDto);

    /**
     * 分页查角色列表
     *
     * @param objectQuery         查询条件
     * @param objectEntityWrapper wapper
     * @return page
     */
    Page selectwithDeptPage(Query<Object> objectQuery, EntityWrapper<Object> objectEntityWrapper);

    /**
     * 更新角色
     *
     * @param roleDto 含有部门信息
     * @return 成功、失败
     */
    Boolean updateRoleById(RoleDTO roleDto);

    /**
     * 通过部门ID查询角色列表
     *
     * @param deptId 部门ID
     * @return 角色列表
     */
    List<SysRole> selectListByDeptId(Integer deptId);
}
