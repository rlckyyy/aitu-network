package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.model.dto.LoginRequest;
import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO) {
        return authService.registerUser(userDTO);
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        authService.login(loginRequest);
    }

    @GetMapping("/secure")
    public String secure(OAuth2AuthenticationToken authentication) {
        return "Authenticated user: " + authentication.getPrincipal().getAttributes();
    }
}
