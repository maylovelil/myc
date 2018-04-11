package com.myc.service;


import com.github.pagehelper.PageInfo;
import com.myc.comm.base.BaseService;
import com.myc.dto.UserDto;
import com.myc.entity.User;
import com.myc.vo.UserVo;

import java.util.List;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/26 18:52
 */
public interface UserService extends BaseService<User> {
    PageInfo<User> selectByPage(User user, int start, int length);

    User selectByUsername(String username);

    void delUser(Integer userid);

    public List<User> selectAll();

    UserVo  selectUserVoByUserId(Integer userId);

    Integer updateVerifyCount(User record);

    PageInfo  selectAllUserVoForPage(UserDto userDto);

}
