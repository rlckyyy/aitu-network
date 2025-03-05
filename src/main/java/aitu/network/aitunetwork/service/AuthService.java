package aitu.network.aitunetwork.service;


import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.config.security.CustomUserDetailsService;
import aitu.network.aitunetwork.config.security.JwtService;
import aitu.network.aitunetwork.model.dto.JwtResponse;
import aitu.network.aitunetwork.model.dto.LoginRequest;
import aitu.network.aitunetwork.model.dto.RegisterRequest;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.enums.Role;
import aitu.network.aitunetwork.repository.SecureTalkUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final SecureTalkUserRepository secureTalkUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final ChatUserService chatUserService;

    public User registerUser(RegisterRequest request) {
        try {
            User user = secureTalkUserRepository.save(mapUserDTOToUser(request));
            chatUserService.saveChatUser(user);
            return user;
        } catch (DuplicateKeyException e) {
            throw new ConflictException("User with email " + request.email() + " already exists");
        }
    }

    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        ));
        var user = customUserDetailsService.
                loadUserByUsername(request.email());
        var jwt = jwtService.generateToken(user);
        return new JwtResponse(jwt);
    }

    public boolean isExist(String email) {
        return secureTalkUserRepository.findUserByEmail(email).isPresent();
    }

    private User mapUserDTOToUser(RegisterRequest userDTO) {
        return User.builder()
                .email(userDTO.email())
                .username(userDTO.username())
                .password(passwordEncoder.encode(userDTO.password()))
                .roles(List.of(Role.USER))
                .friendList(new ArrayList<>())
                .publicKey(userDTO.publicKey())
                .build();
    }
}
