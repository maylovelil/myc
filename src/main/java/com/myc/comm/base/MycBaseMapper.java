package com.myc.comm.base;

import tk.mybatis.mapper.common.*;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/26 19:58
 */
public interface MycBaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
