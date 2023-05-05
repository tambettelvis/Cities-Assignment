package com.tanilsoo.citiesassignment.auth;

import com.tanilsoo.citiesassignment.role.Role;
import com.tanilsoo.citiesassignment.user.User;
import com.tanilsoo.citiesassignment.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean authenticatePassword(LoginDto dto) {
        User user = this.userService.getUserByUsername(dto.getUsername());

        return passwordEncoder.matches(dto.getPassword(), user.getPassword());
    }


    public String generateToken(User user) {


        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(
                this.userService.getUserRoles(user.getId())
                        .stream()
                        .map(Role::getName)
                        .toArray(String[]::new)
        );


        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(user.getUsername())
                .claim("authorities", authorities)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
