package aitu.network.aitunetwork.service;


import aitu.network.aitunetwork.model.dto.UserDTO;

public interface AuthService {
    UserDTO registerUser(UserDTO userDTO);
    void login(UserDTO userDTO);
}
