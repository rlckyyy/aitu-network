package aitu.network.aitunetwork.service;


import aitu.network.aitunetwork.model.dto.LoginRequest;
import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    User registerUser(UserDTO userDTO);

    void login(LoginRequest loginRequest, HttpServletRequest request);
}
