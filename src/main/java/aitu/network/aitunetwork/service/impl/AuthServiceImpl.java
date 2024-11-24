package aitu.network.aitunetwork.service.impl;


import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.service.AuthService;
import aitu.network.aitunetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public void login(UserDTO userDTO) {

    }
}
