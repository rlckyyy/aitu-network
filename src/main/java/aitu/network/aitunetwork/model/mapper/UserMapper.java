package aitu.network.aitunetwork.model.mapper;

import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.dto.UserShortDTO;
import aitu.network.aitunetwork.model.entity.User;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    public static UserShortDTO toShortDto(User user) {
        UserShortDTO dto = new UserShortDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    public static List<UserShortDTO> toShortDtos(List<User> participants) {
        return participants.stream().map(UserMapper::toShortDto).collect(Collectors.toList());
    }
}
