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

    public void setProfilePhoto(MultipartFile file, User user) {
        String hexId = fileService.uploadFile(file);
        user.setAvatar(Avatar.builder()
                .id(hexId)
                .location(fileService.getLinkForResource(hexId))
                .build());
        secureTalkUserRepository.save(user);
    }

    public List<User> getUserFriends(String userId) {
        User user = getById(userId);
        return secureTalkUserRepository.findAllById(user.getFriendList());
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

    public User updateUser(UserUpdateDTO userDTO, User user) {
        user.setUsername(userDTO.username());
        user.setDescription(userDTO.description());
        return secureTalkUserRepository.save(user);
    }

    public void deleteFriendById(String userId, User user) {
        if (userId == null || userId.isBlank()) {
            throw new ConflictException("User id is empty or null");
        }
        if (user.getFriendList() == null || user.getFriendList().isEmpty()) {
            throw new ConflictException("Friend List is empty");
        }
        boolean removed = user.getFriendList().removeIf(userId::equals);
        if (!removed) {
            throw new ConflictException("User doesnt have user with such id");
        }
        secureTalkUserRepository.save(user);
    }

    public List<User> save(Iterable<User> users) {
        return secureTalkUserRepository.saveAll(users);
    }

    public void deleteProfilePhoto(User user) {
        fileService.deleteFile(user.getAvatar().getId());
        user.setAvatar(null);
        secureTalkUserRepository.save(user);
    }
}
