package aitu.network.aitunetwork.service.impl;


import aitu.network.aitunetwork.common.exception.UserAlreadyExistsException;
import aitu.network.aitunetwork.model.dto.LoginRequest;
import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.enums.Role;
import aitu.network.aitunetwork.repository.UserRepository;
import aitu.network.aitunetwork.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public User registerUser(UserDTO userDTO) {
        if (isExist(userDTO.email())) {
            throw new UserAlreadyExistsException("User with email " + userDTO.email() + " already exists");
        }
        return userRepository.save(mapUserDTOToUser(userDTO));
    }

    @Override
    public void login(LoginRequest loginRequest, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (request.getSession(false) == null) {
            request.getSession(true);
        }

        request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );
    }

    public boolean isExist(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    private User mapUserDTOToUser(UserDTO userDTO) {
        return User.builder()
                .email(userDTO.email())
                .username(userDTO.username())
                .password(passwordEncoder.encode(userDTO.password()))
                .roles(List.of(Role.USER))
                .build();
    }

    private static final ThreadLocal<Map<String, String>> cache = ThreadLocal.withInitial(HashMap::new);

    public static void main(String[] args) {
        cache.get().put("hel", "hel");
        cache.get().get("hel");
    }

}
