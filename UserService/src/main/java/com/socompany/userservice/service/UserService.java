package com.socompany.userservice.service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.socompany.core.event.UserCreatedEvent;
import com.socompany.core.event.UserUpdatedEvent;
import com.socompany.userservice.dto.UserRequestDto;
import com.socompany.userservice.dto.UserResponseDto;
import com.socompany.userservice.entity.User;
import com.socompany.userservice.mapper.UserRequestMapper;
import com.socompany.userservice.mapper.UserResponseMapper;
import com.socompany.userservice.repository.UserRepository;
import com.socompany.userservice.util.UserCreationException;
import com.socompany.userservice.util.UserUpdateException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final UserRequestMapper userRequestMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);


    private final KafkaTemplate<String, UserCreatedEvent> userCreateKafkaTemplate;
    private final KafkaTemplate<String, UserUpdatedEvent> userUpdateKafkaTemplate;

    public UserResponseDto createUser(UserRequestDto userEditDto) throws UserCreationException {
        LOGGER.debug("Creating new user");

        User user = userRepository.save(userRequestMapper.map(userEditDto));

        UserCreatedEvent userCreatedEvent = new UserCreatedEvent(
                user.getUsername(),
                user.getPassword(),
                user.getEmail()
        );

        LOGGER.debug("Trying to send user event");

        ProducerRecord<String, UserCreatedEvent> record = new ProducerRecord<>(
                "user-created-events-topic",
                userCreatedEvent
        );

        record.headers().add("message ID", UUID.randomUUID().toString().getBytes());

        try {
            SendResult<String, UserCreatedEvent> sendResult = userCreateKafkaTemplate.send(record).get();
            LOGGER.debug("Sent user event: {}", sendResult);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("Failed to send user event", e);
            Thread.currentThread().interrupt();
            throw new UserCreationException("Failed to create user event", e);
        }

        LOGGER.debug("Successfully created user");
        return userResponseMapper.map(user);
    }

    public Optional<UserResponseDto> updateUser(UserRequestDto userRequestDto, Long id) throws UserUpdateException {
        LOGGER.debug("Trying to update user with id: {}", id);

        UserResponseDto user = userRepository.findById(id).map(mbUser -> userRequestMapper.map(userRequestDto, mbUser))
                .map(userRepository::saveAndFlush).map(userResponseMapper::map).orElseThrow(() -> new UserUpdateException("Failed to update user with id: " + id));

        UserUpdatedEvent userUpdatedEvent = new UserUpdatedEvent(
                user.getUsername(),
                user.getPassword(),
                user.getEmail()
        );

        ProducerRecord<String, UserUpdatedEvent> record = new ProducerRecord<>("user-updated-events-topic", userUpdatedEvent);

        record.headers().add("message ID", UUID.randomUUID().toString().getBytes());

        try {
            SendResult<String, UserUpdatedEvent> sendResult = userUpdateKafkaTemplate.send(record).get();
            LOGGER.debug("Sent user updated event: {}", sendResult);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("Failed to send user updated event", e);
            Thread.currentThread().interrupt();
            throw new UserUpdateException("Failed to update user event", e);
        }

        return Optional.of(user);
    }


    public Optional<UserResponseDto> findById(Long id) {
        LOGGER.debug("Retrieving user with id:{}", id);
        return userRepository.findById(id).map(userResponseMapper::map);
    }

    public Optional<UserResponseDto> findByUsername(String username) {
        LOGGER.debug("Retrieving user with username:{}", username);
        return userRepository.findByUsername(username).map(userResponseMapper::map);
    }

    public boolean deleteUser(Long id) {
        LOGGER.debug("Deleting user with id:{}", id);
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    userRepository.flush();
                    LOGGER.trace("Successfully deleted user with id:{}", id);
                    return true;
                }).orElse(false);
    }

}
