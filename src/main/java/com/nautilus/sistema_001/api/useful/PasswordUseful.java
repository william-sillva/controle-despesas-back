package com.nautilus.sistema_001.api.useful;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUseful {

    private static final Logger log = LoggerFactory.getLogger(PasswordUseful.class);

    public static String generateBCrypt(String password) {
        if (password.isBlank()) {
            return password;
        }

        log.info("Generate hash BCrypt");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

}
