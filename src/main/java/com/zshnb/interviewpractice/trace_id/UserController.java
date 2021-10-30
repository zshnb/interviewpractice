package com.zshnb.interviewpractice.trace_id;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/user/info")
    public HttpServletResponse userInfo(HttpServletResponse response) {
        System.out.println(Thread.currentThread().getName() + " trace id in controller " + Context.CONTEXT.get());
        userService.userInfo();
        return response;
    }
}
