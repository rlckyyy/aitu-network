package aitu.network.aitunetwork.service;


import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.dto.UserUpdateDTO;
import aitu.network.aitunetwork.model.entity.Avatar;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.repository.SecureTalkUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final SecureTalkUserRepository secureTalkUserRepository;
    private final FileService fileService;
    @Value("${secure-talk.domain}")
    private String DOMAIN;

    public void setProfilePhoto(MultipartFile file) {
        User user = getCurrentUser();
        String hexId = fileService.uploadPhoto(file);
        user.setAvatar(Avatar.builder()
                .id(hexId)
                .location(DOMAIN + "/v1/file/" + hexId)
                .build());
        secureTalkUserRepository.save(user);
    }

    public List<User> getUserFriends(String userId) {
        User user = getById(userId);
        return secureTalkUserRepository.findAllByEmailIn(user.getFriendList());
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

    public User getById(String idOrEmail) {
        return secureTalkUserRepository.findByIdOrEmail(idOrEmail)
                .orElseThrow(() -> new EntityNotFoundException(User.class, idOrEmail));
    }

    public User updateUser(UserUpdateDTO userDTO) {
        User currentUser = getCurrentUser();
        currentUser.setUsername(userDTO.username());
        currentUser.setDescription(userDTO.description());
        return secureTalkUserRepository.save(currentUser);
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

    public void deleteProfilePhoto() {
        User currentUser = getCurrentUser();
        fileService.deleteFile(currentUser.getAvatar().getId());
        currentUser.setAvatar(null);
        secureTalkUserRepository.save(currentUser);
    }
}
