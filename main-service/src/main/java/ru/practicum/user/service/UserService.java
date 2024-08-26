package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(NewUserRequest dto);

    void delete(long id);

    List<UserDto> getUsersByIds(List<Long> ids);

    List<UserDto> getAllUsers(int from, int size);
}
