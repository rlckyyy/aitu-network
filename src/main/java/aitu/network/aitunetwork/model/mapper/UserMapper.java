package aitu.network.aitunetwork.model.mapper;

import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.dto.UserShortDTO;
import aitu.network.aitunetwork.model.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO toDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getDescription(),
                user.getEmail(),
                user.getAvatar(),
                user.getRoles(),
                user.getFriendList()
        );
    }

    public static UserShortDTO toShortDto(User user) {
        return new UserShortDTO(user.getId(), user.getUsername(), user.getEmail(), user.getAvatar());
    }

    public static List<UserShortDTO> toShortDtos(List<User> participants) {
        return participants.stream().map(UserMapper::toShortDto).collect(Collectors.toList());
    }
}
