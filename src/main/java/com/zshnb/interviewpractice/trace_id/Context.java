package com.zshnb.interviewpractice.trace_id;

public class Context {
    public static final ThreadLocal<String> CONTEXT = ThreadLocal.withInitial(() -> "");
}
