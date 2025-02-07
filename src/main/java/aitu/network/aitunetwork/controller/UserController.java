package aitu.network.aitunetwork.controller;


import aitu.network.aitunetwork.common.annotations.CurrentUser;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public User hello(@CurrentUser User user) {
        return user;
    }

}
