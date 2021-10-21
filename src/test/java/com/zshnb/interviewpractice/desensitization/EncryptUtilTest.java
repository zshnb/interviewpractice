package com.zshnb.interviewpractice.desensitization;

import com.zshnb.interviewpractice.BaseTest;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EncryptUtilTest extends BaseTest {
    @Autowired
    private EncryptUtil encryptUtil;

    @Test
    public void encrypt() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String key = RandomStringUtils.random(32, true, true);
        String iv = RandomStringUtils.random(16, true, true);
        String content = "hello world";
        String encryptContent = encryptUtil.encrypt(key, iv, content);
        String decryptContent = encryptUtil.decrypt(key, iv, encryptContent);
        assertThat(decryptContent).isEqualTo(content);
    }
}
