package com.myc.mapper;

import com.myc.comm.base.MycBaseMapper;
import com.myc.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/26 18:52
 */
public interface RoleMapper extends MycBaseMapper<Role> {
    public List<Role> queryRoleListWithSelected(Integer id);

    List<Role> queryRoleListByUserIds(@Param("userIds") List<Integer> userIds);
}