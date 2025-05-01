package aitu.network.aitunetwork.service;


import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.model.dto.UserUpdateDTO;
import aitu.network.aitunetwork.model.entity.Avatar;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FileService fileService;

    public void setProfilePhoto(MultipartFile file, User user) {
        String hexId = fileService.uploadFile(file);
        user.setAvatar(Avatar.builder()
                .id(hexId)
                .location(fileService.getLinkForResource(hexId))
                .build());
        userRepository.save(user);
    }

    public List<User> getUserFriends(String userId) {
        User user = getById(userId);
        return userRepository.findAllById(user.getFriendList());
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(String idOrEmail) {
        return userRepository.findByIdOrEmail(idOrEmail)
                .orElseThrow(() -> new EntityNotFoundException(User.class, idOrEmail));
    }

    public User updateUser(UserUpdateDTO userDTO, User user) {
        user.setUsername(userDTO.username());
        user.setDescription(userDTO.description());
        return userRepository.save(user);
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
        userRepository.save(user);
    }

    public List<User> save(Iterable<User> users) {
        return userRepository.saveAll(users);
    }

    public void deleteProfilePhoto(User user) {
        fileService.deleteFile(user.getAvatar().getId());
        user.setAvatar(null);
        userRepository.save(user);
    }
}
