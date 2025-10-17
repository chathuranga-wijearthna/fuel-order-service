package com.assignment.fuelorder.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.assignment.fuelorder.dto.constants.Role;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class JwtUtilTest {

    @Test
    void generateToken_andExtractSubject_shouldRoundTrip() {
        
        String secret = "ABCDEFGHIJKLMNOPQRSTUVXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVXYZ1234567890"; // 64+ bytes
        long ttl = 60_000L;

        JwtUtil jwtUtil = new JwtUtil(secret, ttl);

        var auth = new UsernamePasswordAuthenticationToken("alice", "pwd",
                List.of(new SimpleGrantedAuthority(Role.AIRCRAFT_OPERATOR.name())));

        String token = jwtUtil.generateToken(auth);
        String subject = jwtUtil.getSubject(token);

        assertThat(subject).isEqualTo("alice");
    }
}
