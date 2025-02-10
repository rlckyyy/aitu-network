package aitu.network.aitunetwork.controller;


import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    User me() {
        return userService.getCurrentUser();
    }

    @GetMapping
    List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    User getUserById(@PathVariable String id) {
        return userService.getById(id);
    }

    @PutMapping
    User updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    @PatchMapping("/profile/photo")
    void setProfilePhoto(@RequestParam MultipartFile file) {
        userService.setProfilePhoto(file);
    }

    @DeleteMapping("/friend/{userId}")
    void deleteFriend(@PathVariable String userId) {
        userService.deleteFriendById(userId);
    }

}

