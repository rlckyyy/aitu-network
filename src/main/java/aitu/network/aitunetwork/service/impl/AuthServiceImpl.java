package aitu.network.aitunetwork.service.impl;


import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.common.exception.UserAlreadyExistsException;
import aitu.network.aitunetwork.model.dto.LoginRequest;
import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.enums.Role;
import aitu.network.aitunetwork.repository.UserRepository;
import aitu.network.aitunetwork.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
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

}
