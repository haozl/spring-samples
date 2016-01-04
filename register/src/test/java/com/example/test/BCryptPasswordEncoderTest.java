package com.example.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BCryptPasswordEncoderTest {

    @Test
    public void encode() {
        String password = "123";

        for (int i = 0; i < 5; i++) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashed = encoder.encode(password);
            System.out.println(hashed);
            Assert.assertTrue(encoder.matches(password, hashed));
        }

    }

    @Test
    public void strength() {
        String password = "123";

        BCryptPasswordEncoder encoder10 = new BCryptPasswordEncoder(10);
        BCryptPasswordEncoder encoder12 = new BCryptPasswordEncoder(12);

        String hashed = encoder10.encode(password);
        System.out.println("[strength: 10]" + hashed);
        Assert.assertTrue(encoder10.matches(password, hashed));
        Assert.assertTrue(encoder12.matches(password, hashed));

        hashed = encoder12.encode(password);
        System.out.println("[strength: 12]" + hashed);
        Assert.assertTrue(encoder10.matches(password, hashed));
        Assert.assertTrue(encoder12.matches(password, hashed));
    }

    @Configuration
    static class config {

    }
}
