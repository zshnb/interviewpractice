package com.zshnb.interviewpractice.trace_id;

import org.springframework.stereotype.Service;

@Service("traceIdUserService")
public class UserService {
    public void userInfo() {
        System.out.println(Thread.currentThread().getName() + " trace id in service " + Context.CONTEXT.get());
    }
}
