package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.common.annotations.CurrentUser;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.dto.JwtResponse;
import aitu.network.aitunetwork.model.dto.LoginRequest;
import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.service.AuthService;
import aitu.network.aitunetwork.util.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
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
    public User register(@RequestBody @Valid UserDTO userDTO) {
        return authService.registerUser(userDTO);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Valid LoginRequest loginRequest,
                             HttpServletResponse response) {
        JwtResponse jwtResponse = authService.login(loginRequest);
        CookieUtils.addJwtToCookie(response, jwtResponse.token());
        return jwtResponse;
    }

    @GetMapping("/me")
    public UserDTO me(@CurrentUser UserDetails userDetails) {
        User user = ((CustomUserDetails) userDetails).getUser();
        return new UserDTO(user.getUsername(), user.getEmail(), "");
    }
}
