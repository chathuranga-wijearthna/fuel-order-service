package com.assignment.fuelorder.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.assignment.fuelorder.dto.AuthDtos;
import com.assignment.fuelorder.dto.constants.Role;
import com.assignment.fuelorder.entity.User;
import com.assignment.fuelorder.exception.CustomGlobalException;
import com.assignment.fuelorder.repo.UserRepository;
import com.assignment.fuelorder.security.JwtUtil;
import com.assignment.fuelorder.service.impl.AuthServiceImpl;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl service;

    @Test
    void login_shouldAuthenticate_andReturnToken() {
        
        AuthDtos.LoginRequest req = new AuthDtos.LoginRequest("user@example.com", "secret");
        Authentication auth = new UsernamePasswordAuthenticationToken("user@example.com", "secret", List.of(new SimpleGrantedAuthority("ROLE_X")));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtUtil.generateToken(eq(auth))).thenReturn("jwt-token");

        AuthDtos.TokenResponse resp = service.login(req);

        assertThat(resp.token()).isEqualTo("jwt-token");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(eq(auth));
    }

    @Test
    void register_shouldSaveUser_andReturnId() {
        
        AuthDtos.RegisterRequest req = new AuthDtos.RegisterRequest("new@example.com", "p@ss", Role.AIRCRAFT_OPERATOR);
        when(userRepository.existsByEmailIgnoreCase("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("p@ss")).thenReturn("HASH");

        AtomicLong idGen = new AtomicLong(100L);
        // simulate JPA setting ID on save
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(idGen.get());
            return u;
        });

        Long id = service.register(req);

        assertThat(id).isEqualTo(100L);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getEmail()).isEqualTo("new@example.com");
        assertThat(saved.getPasswordHash()).isEqualTo("HASH");
        assertThat(saved.getUserRole()).isEqualTo(Role.AIRCRAFT_OPERATOR);
    }

    @Test
    void register_shouldThrow_whenEmailExists() {
        
        AuthDtos.RegisterRequest req = new AuthDtos.RegisterRequest("dup@example.com", "p", Role.OPERATIONS_MANAGER);
        when(userRepository.existsByEmailIgnoreCase("dup@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.register(req))
                .isInstanceOf(CustomGlobalException.class)
                .hasMessageContaining("Email already registered");
        verify(userRepository, never()).save(any());
    }
}
