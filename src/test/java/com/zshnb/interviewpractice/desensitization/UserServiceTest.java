package com.zshnb.interviewpractice.desensitization;

import com.zshnb.interviewpractice.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserServiceTest extends BaseTest {
    @Autowired
    private UserService userService;

    @Test
    public void createSuccessful() {
        User user = userService.createUser("user", "17858936099", "330327198776549876");
        assertThat(user.getPhone()).isEqualTo("1785893****");
        assertThat(user.getCardNumber()).isEqualTo("330327********9876");
    }

    @Test
    public void getUserSuccessful() {
        User user = userService.createUser("user", "17858936099", "330327198776549876");
        assertThat(user.getPhone()).isEqualTo("1785893****");
        assertThat(user.getCardNumber()).isEqualTo("330327********9876");
        user = userService.getUser("user");
        assertThat(user.getPhone()).isEqualTo("17858936099");
        assertThat(user.getCardNumber()).isEqualTo("330327198776549876");
    }
}
