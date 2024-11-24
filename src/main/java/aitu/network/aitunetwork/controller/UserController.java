package aitu.network.aitunetwork.controller;


import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    // angular -> user ->
    @GetMapping("/save")
    public User saveUser(@RequestBody UserDTO userDTO) {
        return userService.save(userDTO);
    }
    // getUser() -> jwt -> email -> if
    // frontend -> keycloak -> frontend -> backend(getUser) -> UserRespDto
}
