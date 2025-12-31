package com.smartclassroom.backend.service;

import com.smartclassroom.backend.dto.AuthRequest;
import com.smartclassroom.backend.dto.AuthResponse;
import com.smartclassroom.backend.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        // Strip "ROLE_" prefix if present for cleaner frontend consumption (optional)
        if (role != null && role.startsWith("ROLE_")) {
            role = role.substring(5);
        }

        return AuthResponse.builder()
                .token(token)
                .role(role)
                .build();
    }
}
