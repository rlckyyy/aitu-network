package aitu.network.aitunetwork.controller;


import aitu.network.aitunetwork.common.annotations.CurrentUser;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public User hello(@CurrentUser User user) {
        return user;
    }

    @PatchMapping("/profile/photo")
    public User setProfilePhoto(@RequestParam MultipartFile file) {
        userService.setProfilePhoto(file);
        return null;
    }
}
// TODO friendship-system
// TODO add-delete-td-friend
// TODO POSTING SYSTEM and PROFILE

