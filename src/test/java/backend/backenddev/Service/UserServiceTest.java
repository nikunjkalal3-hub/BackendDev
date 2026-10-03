package backend.backenddev.Service;

import backend.backenddev.entity.User;
import backend.backenddev.exception.EmailAlreadyExistsException;
import backend.backenddev.exception.UserNotFoundException;
import backend.backenddev.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers_shouldReturnAllUsers() {

        User user1 = new User(
                "User One",
                "user1@example.com",
                "Password@123"
        );

        User user2 = new User(
                "User Two",
                "user2@example.com",
                "Password@123"
        );

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("user1@example.com", result.get(0).getEmail());
        assertEquals("user2@example.com", result.get(1).getEmail());

        verify(userRepository).findAll();
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {

        User user = new User(
                "Test User",
                "test@example.com",
                "Password@123"
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals(
                "Test User",
                result.getName()
        );

        assertEquals(
                "test@example.com",
                result.getEmail()
        );

        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(99L)
        );

        verify(userRepository).findById(99L);
    }

    @Test
    void updateUser_shouldUpdateNameAndEmail() {

        User existingUser = new User(
                "Old Name",
                "old@example.com",
                "Password@123"
        );

        User updateRequest = new User(
                "New Name",
                "new@example.com",
                null
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.existsByEmail("new@example.com"))
                .thenReturn(false);

        when(userRepository.save(existingUser))
                .thenReturn(existingUser);

        User result = userService.updateUser(1L, updateRequest);

        assertEquals(
                "New Name",
                result.getName()
        );

        assertEquals(
                "new@example.com",
                result.getEmail()
        );

        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("new@example.com");
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUser_shouldThrowException_whenUserDoesNotExist() {

        User updateRequest = new User(
                "New Name",
                "new@example.com",
                null
        );

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(99L, updateRequest)
        );

        verify(userRepository).findById(99L);

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void updateUser_shouldThrowException_whenEmailAlreadyExists() {

        User existingUser = new User(
                "Old Name",
                "old@example.com",
                "Password@123"
        );

        User updateRequest = new User(
                "New Name",
                "taken@example.com",
                null
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.existsByEmail("taken@example.com"))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.updateUser(1L, updateRequest)
        );

        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("taken@example.com");

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void deleteUser_shouldDeleteUser_whenUserExists() {

        User user = new User(
                "Test User",
                "test@example.com",
                "Password@123"
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_shouldThrowException_whenUserDoesNotExist() {

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(99L)
        );

        verify(userRepository).findById(99L);

        verify(userRepository, never())
                .delete(any(User.class));
    }
}