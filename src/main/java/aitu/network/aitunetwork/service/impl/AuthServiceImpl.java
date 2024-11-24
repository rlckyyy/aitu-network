package aitu.network.aitunetwork.service.impl;


import aitu.network.aitunetwork.common.exception.UserAlreadyExistsException;
import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.repository.UserRepository;
import aitu.network.aitunetwork.service.AuthService;
import aitu.network.aitunetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserDTO userDTO) {
        if (userRepository.findUserByEmail(userDTO.email()) != null) {
            throw new UserAlreadyExistsException("User with email " + userDTO.email() + " already exists");
        }
        return userRepository.save(mapUserDTOToUser(userDTO));
    }

    @Override
    public void login(UserDTO userDTO) {

    }

    private User mapUserDTOToUser(UserDTO userDTO) {
        return User.builder()
                .username(userDTO.username())
                .password(passwordEncoder.encode(userDTO.password()))
                .email(userDTO.email())
                .build();
    }

}
