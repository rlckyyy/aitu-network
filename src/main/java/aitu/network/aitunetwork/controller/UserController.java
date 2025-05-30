package aitu.network.aitunetwork.controller;


import aitu.network.aitunetwork.common.annotations.CurrentUser;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.dto.UserShortDTO;
import aitu.network.aitunetwork.model.dto.UserUpdateDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/public/{id}")
    User getUserById(@PathVariable String id) {
        return userService.getById(id);
    }

    @GetMapping("/public/search")
    List<User> searchUsers(@RequestParam String query, @CurrentUser(required = false) CustomUserDetails userDetails) {
        return userService.searchUsers(query, userDetails);
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
    void deleteFriend(@PathVariable @NotBlank String userId, @CurrentUser CustomUserDetails user) {
        userService.deleteFriendById(userId, user.user());
    }

    @GetMapping("/related")
    Collection<UserShortDTO> getRelatedUsers(@CurrentUser CustomUserDetails user) {
        return userService.getRelatedUsers(user.user());
    }
}

