package tn.smartcaisse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import tn.smartcaisse.dto.*;
import tn.smartcaisse.repository.UserRepository;
import tn.smartcaisse.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(ApiResponse.error("Identifiants incorrects"));
        }

        var userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        var token = jwtUtil.generateToken(userDetails);

        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        return ResponseEntity.ok(ApiResponse.ok(AuthResponse.builder()
            .token(token)
            .username(user.getUsername())
            .role(user.getRole().name())
            .fullName(user.getFullName())
            .build()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse>> me(
            org.springframework.security.core.Authentication auth) {
        var user = userRepository.findByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(ApiResponse.ok(AuthResponse.builder()
            .username(user.getUsername())
            .role(user.getRole().name())
            .fullName(user.getFullName())
            .build()));
    }
}
