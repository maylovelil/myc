package com.myc.service;


import com.github.pagehelper.PageInfo;
import com.myc.comm.base.BaseService;
import com.myc.entity.Role;

import java.util.List;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/26 18:52
 */
public interface RoleService extends BaseService<Role> {

    public List<Role> queryRoleListWithSelected(Integer uid);

    PageInfo<Role> selectByPage(Role role, int start, int length);

    /**
     * 删除角色 同时删除角色资源表中的数据
     *
     * @param roleid
     */
    public void delRole(Integer roleid);
}
