package com.myc.comm.utils;

import com.myc.comm.ServiceException;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/15 19:22
 */
public class ExceptionUtils {

    public static void throwServiceError(String errorMessage){
        throw new ServiceException(errorMessage);
    }

}
