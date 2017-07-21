package com.config.security;

import com.rest.OAuth2Util;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Julia on 18.07.2017.
 */
public class OAuth2TokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = OAuth2Util.tokenValue();
        if (token != null) {
            String tokenStr = String.join(" ", OAuth2Util.tokenType(), token);
            response.addHeader("Authorization", tokenStr);
        }
        filterChain.doFilter(request, response);
    }
}
