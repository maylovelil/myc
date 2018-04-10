package com.myc.comm.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/4/10 14:40
 */
@Component(value = "mycDefaultSubjectFactory")
public class MycDefaultSubjectFactory  extends DefaultWebSubjectFactory{
    @Override
    public Subject createSubject(SubjectContext context) {
        //不创建session
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
