package com.zshnb.interviewpractice.oauth2;

import com.nimbusds.oauth2.sdk.AuthorizationResponse;
import com.nimbusds.oauth2.sdk.AuthorizationSuccessResponse;
import com.zshnb.interviewpractice.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class OAuthServerTest extends BaseTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void authorizeSuccessful() {
        ResponseEntity<Object> response =
            restTemplate.getForEntity("/oauth2/authorization?clientId=1&scope=scope&callback=http://baidu.com", Object.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//        Assertions.assertNotNull();
    }
}
