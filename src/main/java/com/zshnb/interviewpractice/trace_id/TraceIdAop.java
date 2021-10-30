package com.zshnb.interviewpractice.trace_id;

import java.util.UUID;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class TraceIdAop {
    @Pointcut("execution(* com.zshnb.interviewpractice.trace_id.UserController.*(..))")
    public void execute() {}

    @Before("execute()")
    public void setTraceId() {
        Context.CONTEXT.set(UUID.randomUUID().toString());
    }

    @After("execute()")
    public void clearTraceId() {
        Context.CONTEXT.remove();
    }
}
