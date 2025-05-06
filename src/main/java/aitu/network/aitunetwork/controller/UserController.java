package aitu.network.aitunetwork.controller;


import aitu.network.aitunetwork.common.annotations.CurrentUser;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.dto.UserShortDTO;
import aitu.network.aitunetwork.model.dto.UserUpdateDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/public/{id}")
    User getUserById(@PathVariable String id) {
        return userService.getById(id);
    }

    @PatchMapping
    User updateUser(@RequestBody UserUpdateDTO userDTO, @CurrentUser CustomUserDetails user) {
        return userService.updateUser(userDTO, user.user());
    }

    @PatchMapping("/profile/photo")
    void setProfilePhoto(@RequestParam MultipartFile file, @CurrentUser CustomUserDetails user) {
        userService.setProfilePhoto(file, user.user());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/profile/photo")
    void deleteProfilePhoto(@CurrentUser CustomUserDetails user) {
        userService.deleteProfilePhoto(user.user());
    }

    @DeleteMapping("/friend/{userId}")
    void deleteFriend(@PathVariable String userId, @CurrentUser CustomUserDetails user) {
        userService.deleteFriendById(userId, user.user());
    }

    @GetMapping("/related")
    public Collection<UserShortDTO> getRelatedUsers(@CurrentUser CustomUserDetails user) {
        return userService.getRelatedUsers(user.user());
    }
}

