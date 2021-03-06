package com.myc.mapper;


import com.myc.comm.base.MycBaseMapper;
import com.myc.dto.UserDto;
import com.myc.entity.User;
import com.myc.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/26 18:52
 */
public interface UserMapper extends MycBaseMapper<User> {
    UserVo selectUserVoByUserId(Integer userId);

    List<User>  selectAllUser(@Param("userDto") UserDto userDto);
}