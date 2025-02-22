package aitu.network.aitunetwork.model.mapper;

import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;

public class UserMapper {
    public static UserDTO toDto(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getDescription(), user.getEmail(), user.getRoles(), user.getFriendList());
    }
}
