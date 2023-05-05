package com.tanilsoo.citiesassignment.auth;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import com.tanilsoo.citiesassignment.exception.UnauthorizedException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class PatchRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !HttpMethod.PATCH.matches(request.getMethod());
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = parseJwt(request);

        Jwt jwt = jwtDecoder.decode(token);

        var authorities = (ArrayList<LinkedTreeMap>) jwt.getClaims().get("authorities");
        var roles = (String) authorities.get(0).get("role");

        if (!roles.contains("ALLOW_EDIT")) {
            log.info("Unauthorized user");
            throw new UnauthorizedException("You are not authorized to edit this resource");
        }

        log.info("Authorized user");
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }


}
