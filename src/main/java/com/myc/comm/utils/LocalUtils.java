package com.myc.comm.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/14 18:39
 */
@Component
public class LocalUtils {
    @Autowired
    protected MessageSource messageSource;

    private static MessageSource ms;

    @PostConstruct
    public void initialize() {
        //@Component will call construct
        ms = this.messageSource;
    }

    public  static String getLocal(String code,Object[] args){
        Locale locale = LocaleContextHolder.getLocale();
        String msg = ms.getMessage(code, args,locale);
        return msg;
    }
}
