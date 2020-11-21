package com.rdesouter.security;

import com.rdesouter.config.AppConfiguration;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {

    public SecurityConstants(AppConfiguration appConfiguration) {
        SECRET = appConfiguration.getJwtSecret();
        EXPIRATION_TIME = appConfiguration.getTokenExpirationTime();
    }

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static String SECRET;
    public static long EXPIRATION_TIME;

}
