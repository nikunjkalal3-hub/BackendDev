package backend.backenddev.Controller;

import backend.backenddev.Service.AuthService;
import backend.backenddev.Service.UserService;
import backend.backenddev.dto.RegisterRequest;
import backend.backenddev.dto.UpdateUserReq;
import backend.backenddev.dto.UserResponse;
import backend.backenddev.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserController userController;

    private User createUser(
            Long id,
            String name,
            String email,
            String password
    ) throws Exception {

        User user = new User(name, email, password);

        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, id);

        return user;
    }

    @Test
    void createdUser_shouldCreateAndReturnUser() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("Password@123");

        User createdUser = createUser(
                1L,
                "Test User",
                "test@example.com",
                "HASHED_PASSWORD"
        );

        when(authService.register(any(User.class)))
                .thenReturn(createdUser);

        ResponseEntity<UserResponse> response =
                userController.createdUser(request);

        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(
                "Test User",
                response.getBody().getName()
        );

        assertEquals(
                "test@example.com",
                response.getBody().getEmail()
        );

        assertEquals(
                1L,
                response.getBody().getId()
        );

        verify(authService)
                .register(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() throws Exception {

        User user1 = createUser(
                1L,
                "User One",
                "user1@example.com",
                "Password@123"
        );

        User user2 = createUser(
                2L,
                "User Two",
                "user2@example.com",
                "Password@123"
        );

        when(userService.getAllUsers())
                .thenReturn(List.of(user1, user2));

        ResponseEntity<List<UserResponse>> response =
                userController.getAllUsers();

        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(2, response.getBody().size());

        assertEquals(
                "user1@example.com",
                response.getBody().get(0).getEmail()
        );

        assertEquals(
                "user2@example.com",
                response.getBody().get(1).getEmail()
        );

        assertEquals(
                1L,
                response.getBody().get(0).getId()
        );

        assertEquals(
                2L,
                response.getBody().get(1).getId()
        );

        verify(userService)
                .getAllUsers();
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {

        User user = createUser(
                1L,
                "Test User",
                "test@example.com",
                "Password@123"
        );

        when(userService.getUserById(1L))
                .thenReturn(user);

        ResponseEntity<UserResponse> response =
                userController.getUserById(1L);

        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(
                1L,
                response.getBody().getId()
        );

        assertEquals(
                "Test User",
                response.getBody().getName()
        );

        assertEquals(
                "test@example.com",
                response.getBody().getEmail()
        );

        verify(userService)
                .getUserById(1L);
    }

    @Test
    void updateUser_shouldUpdateAndReturnUser() throws Exception {

        UpdateUserReq request = new UpdateUserReq();
        request.setName("Updated Name");
        request.setEmail("updated@example.com");

        User updatedUser = createUser(
                1L,
                "Updated User",
                "updated@example.com",
                null
        );

        when(userService.updateUser(
                any(Long.class),
                any(User.class)
        )).thenReturn(updatedUser);

        ResponseEntity<UserResponse> response =
                userController.updateUser(1L, request);

        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(
                1L,
                response.getBody().getId()
        );

        assertEquals(
                "Updated User",
                response.getBody().getName()
        );

        assertEquals(
                "updated@example.com",
                response.getBody().getEmail()
        );

        verify(userService)
                .updateUser(
                        any(Long.class),
                        any(User.class)
                );
    }

    @Test
    void deleteUser_shouldReturnNoContent() {

        ResponseEntity<Void> response =
                userController.deleteUser(1L);

        assertEquals(204, response.getStatusCode().value());

        verify(userService)
                .deleteUser(1L);
    }
}