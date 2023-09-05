package ru.practicum.user.service.classes;

import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

import ru.practicum.user.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto saveUser(UserDto outputUserDto) {
        log.info("Запрос на создание пользователя успешно передан в сервис UserServiceImpl");
        User user = userMapper.userDtoToUser(outputUserDto);
        user = userRepository.save(user);
        log.info("Пользователь: {} успешно сохранен", user);
        return userMapper.userToUserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Запрос на удаление пользователя успешно передан в сервис UserServiceImpl");
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователя с id: {} не существует", userId);
            throw new NotFoundException("Пользователь с id: " + userId + " не существует");
        }
        userRepository.deleteById(userId);
        log.info("Пользователь с id: {} успешно удален", userId);
    }

    @Override
    public List<UserDto> getUsersInfo(List<Long> ids, int from, int size) {
        log.info("Запрос на получение списка пользователей успешно передан в сервис UserServiceImpl");
        PageRequest pagination = PageRequest.of(from / size, size);
        List<User> listOfUsers = new ArrayList<>();
        if (ids == null) {
            listOfUsers.addAll(userRepository.findAll(pagination).getContent());
        } else {
            listOfUsers.addAll(userRepository.findAllByIdIn(ids, pagination));
        }
        log.info("Список пользователей: {} успешно получен", listOfUsers);
        return userMapper.userListToUserDtoList(listOfUsers);
    }
}
