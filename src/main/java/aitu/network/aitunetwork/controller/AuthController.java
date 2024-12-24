package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.model.dto.LoginRequest;
import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
