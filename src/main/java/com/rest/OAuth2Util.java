package com.rest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

/**
 * Created by Julia on 17.07.2017.
 */
public class OAuth2Util {

    public static String tokenValue() {
        OAuth2AuthenticationDetails details = getDetails();
        return details == null ? null : details.getTokenValue();
    }

    public static String tokenType() {
        OAuth2AuthenticationDetails details = getDetails();
        return details == null ? null : details.getTokenType();
    }

    private static OAuth2AuthenticationDetails getDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication instanceof OAuth2Authentication))
            return null;
        return (OAuth2AuthenticationDetails) authentication.getDetails();
    }


}
