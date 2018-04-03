package com.myc.mapper;


import com.myc.comm.base.MycBaseMapper;
import com.myc.entity.UserRole;

import java.util.List;
/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/26 18:52
 */
public interface UserRoleMapper extends MycBaseMapper<UserRole> {
    public List<Integer> findUserIdByRoleId(Integer roleId);
}