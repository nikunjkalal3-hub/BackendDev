package backend.backenddev.Service;

import backend.backenddev.entity.User;
import backend.backenddev.exception.EmailAlreadyExistsException;
import backend.backenddev.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldCreateUser() {

        User user = new User(
                "Test User",
                "test@example.com",
                "Password@123"
        );

        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("Password@123"))
                .thenReturn("HASHED_PASSWORD");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = authService.register(user);

        assertEquals(
                "test@example.com",
                result.getEmail()
        );

        assertEquals(
                "HASHED_PASSWORD",
                result.getPassword()
        );

        verify(userRepository)
                .existsByEmail("test@example.com");

        verify(passwordEncoder)
                .encode("Password@123");

        verify(userRepository)
                .save(user);
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {

        User user = new User(
                "Test User",
                "test@example.com",
                "Password@123"
        );

        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.register(user)
        );

        verify(userRepository)
                .existsByEmail("test@example.com");

        verify(userRepository, never())
                .save(any(User.class));

        verify(passwordEncoder, never())
                .encode(any(String.class));
    }

    @Test
    void register_shouldEncodePasswordBeforeSaving() {

        User user = new User(
                "Test User",
                "test@example.com",
                "Password@123"
        );

        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("Password@123"))
                .thenReturn("HASHED_PASSWORD");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = authService.register(user);

        assertEquals(
                "HASHED_PASSWORD",
                result.getPassword()
        );

        verify(passwordEncoder)
                .encode("Password@123");

        verify(userRepository)
                .save(user);
    }
}