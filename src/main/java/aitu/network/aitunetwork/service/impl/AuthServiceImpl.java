package aitu.network.aitunetwork.service.impl;


import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.model.dto.JwtResponse;
import aitu.network.aitunetwork.model.dto.LoginRequest;
import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.enums.Role;
import aitu.network.aitunetwork.repository.UserRepository;
import aitu.network.aitunetwork.config.security.CustomUserDetailsService;
import aitu.network.aitunetwork.config.security.JwtService;
import aitu.network.aitunetwork.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public User registerUser(UserDTO userDTO) {
        if (isExist(userDTO.email())) {
            throw new ConflictException("User with email " + userDTO.email() + " already exists");
        }
        return userRepository.save(mapUserDTOToUser(userDTO));
    }

    @Override
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



    @Override
    public User getByUsername(String email) {
        return userRepository
                .findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(User.class, email));
    }

    @Override
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

}
