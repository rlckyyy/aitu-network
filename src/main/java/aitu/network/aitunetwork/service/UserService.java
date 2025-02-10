package aitu.network.aitunetwork.service;


import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.repository.SecureTalkUserRepository;
import aitu.network.aitunetwork.service.util.GridFsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final SecureTalkUserRepository secureTalkUserRepository;
    private final GridFsService gridFsService;

    public void setProfilePhoto(MultipartFile file) {
        try {
            User user = getCurrentUser();
            String hexId = gridFsService.uploadPhoto(file);
            user.setPhotoPath(hexId);
            secureTalkUserRepository.save(user);
        } catch (IOException e) {
            throw new ConflictException(e.getLocalizedMessage());
        }
    }

    public User getCurrentUser() {
        var principal = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return secureTalkUserRepository.findUserByEmail(principal.getUsername()).orElseThrow(() ->
                new EntityNotFoundException(User.class, "email", principal.getUsername()));
    }

    public List<User> getAll() {
        return secureTalkUserRepository.findAll();
    }

    public User getById(String id) {
        return secureTalkUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    public User updateUser(UserDTO userDTO) {
        return null;
    }

    public void deleteFriendById(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new ConflictException("User id is empty or null");
        }
        User user = getCurrentUser();
        if (user.getFriendList() == null || user.getFriendList().isEmpty()) {
            throw new ConflictException("Friend List is empty");
        }
        boolean removed = user.getFriendList().removeIf(userId::equals);
        if (!removed) {
            throw new ConflictException("User doesnt have user with such id");
        }
        secureTalkUserRepository.save(user);
    }

    public User save(User user) {
        return secureTalkUserRepository.save(user);
    }
}
