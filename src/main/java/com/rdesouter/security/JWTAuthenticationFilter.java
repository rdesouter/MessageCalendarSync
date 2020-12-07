package com.rdesouter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.rdesouter.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {

        try {
            UsernamePassword creds = new ObjectMapper()
                    .readValue(req.getInputStream(), UsernamePassword.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.username,
                            creds.password,
                            new ArrayList<>()
                    )
            );

        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain,
            Authentication auth
    ) throws IOException {
        User user = (User) auth.getPrincipal();
        List<String> roles = new ArrayList<>();
        for(GrantedAuthority grantedAuthority: user.getAuthorities()){
            roles.add(grantedAuthority.getAuthority());
        }

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        res.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter pw = res.getWriter();
        pw.write("\"" + token + "\"");
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        res.addHeader("Content-Type", "application/json");
    }
}
