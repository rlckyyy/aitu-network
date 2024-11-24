package aitu.network.aitunetwork.service;


import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;

public interface AuthService {
    User registerUser(UserDTO userDTO);
    void login(UserDTO userDTO);
}
