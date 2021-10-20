package com.zshnb.interviewpractice.desensitization;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class UserService {
    private final UserDao userDao;
    private final EncryptUtil encryptUtil;
    private final SecretDataDao secretDataDao;

    public UserService(UserDao userDao, EncryptUtil encryptUtil, SecretDataDao secretDataDao) {
        this.userDao = userDao;
        this.encryptUtil = encryptUtil;
        this.secretDataDao = secretDataDao;
    }

    @Transactional
    public User createUser(String name, String phone, String cardNumber) {
        String key = RandomStringUtils.random(32, true, true);
        String iv = RandomStringUtils.random(16, true, true);
        User user = new User();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phone", phone);
        jsonObject.addProperty("cardNumber", cardNumber);
        user.setName(name);
        user.setPhone(StringUtils.rightPad(phone.substring(0, phone.length() - 4), 11, "*"));
        try {
            user.setEncryptData(encryptUtil.encrypt(key, iv, jsonObject.toString()));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        char[] cardNumberChars = cardNumber.toCharArray();
        System.arraycopy(StringUtils.repeat("*", 8).toCharArray(), 0, cardNumberChars, 6, 8);
        user.setCardNumber(String.valueOf(cardNumberChars));
        user = userDao.save(user);

        SecretData secretData = new SecretData();
        secretData.setUser(user);
        secretData.setKey(key);
        secretData.setIv(iv);
        secretDataDao.save(secretData);
        return user;
    }

    public User getUser(String name) {
        User user = userDao.findByName(name);
        SecretData secretData = secretDataDao.findByUser(user);
        try {
            String decryptInfo = encryptUtil.decrypt(secretData.getKey(), secretData.getIv(), user.getEncryptData());
            JsonObject jsonObject = new Gson().fromJson(decryptInfo, JsonObject.class);
            user.setPhone(jsonObject.get("phone").getAsString());
            user.setCardNumber(jsonObject.get("cardNumber").getAsString());
            return user;
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
