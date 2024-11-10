package edu.java.resttask.service;

import edu.java.resttask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserServiceTest {

    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        userService = new UserService(userRepository);
    }

    @Test
    void findByUserName() {
        userService.findByUserName(anyString());

        verify(userRepository).findByUsername(anyString());
    }

    @Test
    void findByUsernameAndPassword() {
        userService.findByUsernameAndPassword(anyString(), anyString());

        verify(userRepository).findByUsernameAndPassword(anyString(), anyString());
    }

    @Test
    void changePassword() {
    }
}