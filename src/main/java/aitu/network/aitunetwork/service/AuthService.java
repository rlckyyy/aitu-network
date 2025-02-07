package aitu.network.aitunetwork.service;


import aitu.network.aitunetwork.model.dto.JwtResponse;
import aitu.network.aitunetwork.model.dto.LoginRequest;
import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;

public interface AuthService {
    User registerUser(UserDTO userDTO);

    JwtResponse login(LoginRequest loginRequest);

    User getByUsername(String username);

    boolean isExist(String email);
}
