package com.tanilsoo.citiesassignment.configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.tanilsoo.citiesassignment.auth.PatchRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebMvc
public class SecurityConfig {
    private final RSAPublicKey pubKey;
    private final RSAPrivateKey privateKey;

    public SecurityConfig() throws JOSEException {
        RSAKeyGenerator rsa = new RSAKeyGenerator(2048);
        RSAKey rsaKeyGen = rsa.generate();
        pubKey = rsaKeyGen.toRSAPublicKey();
        privateKey = rsaKeyGen.toRSAPrivateKey();
    }

    @Bean
    public PatchRequestFilter patchRequestFilter() {
        return new PatchRequestFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.PATCH,"/**").authenticated()
                .requestMatchers("/**").permitAll()
                .and()
                .addFilterAfter(patchRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer((oauth2ResourceServer) ->
                                            oauth2ResourceServer.jwt((jwt) -> jwt.decoder(jwtDecoder())
                ));

        http.csrf().disable();


        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(pubKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(pubKey).build();
    }

}
