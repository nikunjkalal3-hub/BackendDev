package backend.backenddev.Controller;

import backend.backenddev.Service.AuthService;
import backend.backenddev.Service.UserService;
import backend.backenddev.dto.RegisterRequest;
import backend.backenddev.dto.UpdateUserReq;
import backend.backenddev.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserControllerSecurityTest {

    @Autowired
    private UserController userController;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(
            username = "admin@example.com",
            roles = "ADMIN"
    )
    void admin_shouldCreateUser() {

        RegisterRequest request = new RegisterRequest();

        request.setName("New User");
        request.setEmail("new@example.com");
        request.setPassword("Password@123");

        User createdUser = new User(
                "New User",
                "new@example.com",
                "HASHED_PASSWORD"
        );

        when(authService.register(any(User.class)))
                .thenReturn(createdUser);

        assertDoesNotThrow(
                () -> userController.createdUser(request)
        );
    }

    @Test
    @WithMockUser(
            username = "user@example.com",
            roles = "USER"
    )
    void user_shouldNotCreateUser() {

        RegisterRequest request = new RegisterRequest();

        request.setName("New User");
        request.setEmail("new@example.com");
        request.setPassword("Password@123");

        assertThrows(
                AccessDeniedException.class,
                () -> userController.createdUser(request)
        );
    }

    @Test
    @WithMockUser(
            username = "user@example.com",
            roles = "USER"
    )
    void user_shouldGetAllUsers() {

        when(userService.getAllUsers())
                .thenReturn(java.util.List.of());

        assertDoesNotThrow(
                () -> userController.getAllUsers()
        );
    }

    @Test
    @WithMockUser(
            username = "admin@example.com",
            roles = "ADMIN"
    )
    void admin_shouldGetAllUsers() {

        when(userService.getAllUsers())
                .thenReturn(java.util.List.of());

        assertDoesNotThrow(
                () -> userController.getAllUsers()
        );
    }

    @Test
    @WithMockUser(
            username = "user@example.com",
            roles = "USER"
    )
    void user_shouldGetUserById() {

        User user = new User(
                "Test User",
                "test@example.com",
                "Password@123"
        );

        when(userService.getUserById(1L))
                .thenReturn(user);

        assertDoesNotThrow(
                () -> userController.getUserById(1L)
        );
    }

    @Test
    @WithMockUser(
            username = "admin@example.com",
            roles = "ADMIN"
    )
    void admin_shouldGetUserById() {

        User user = new User(
                "Test User",
                "test@example.com",
                "Password@123"
        );

        when(userService.getUserById(1L))
                .thenReturn(user);

        assertDoesNotThrow(
                () -> userController.getUserById(1L)
        );
    }

    @Test
    @WithMockUser(
            username = "user@example.com",
            roles = "USER"
    )
    void user_shouldNotUpdateUser() {

        UpdateUserReq request = new UpdateUserReq();

        request.setName("Updated User");
        request.setEmail("updated@example.com");

        assertThrows(
                AccessDeniedException.class,
                () -> userController.updateUser(1L, request)
        );
    }

    @Test
    @WithMockUser(
            username = "admin@example.com",
            roles = "ADMIN"
    )
    void admin_shouldUpdateUser() {

        UpdateUserReq request = new UpdateUserReq();

        request.setName("Updated User");
        request.setEmail("updated@example.com");

        User updatedUser = new User(
                "Updated User",
                "updated@example.com",
                null
        );

        when(userService.updateUser(
                any(Long.class),
                any(User.class)
        )).thenReturn(updatedUser);

        assertDoesNotThrow(
                () -> userController.updateUser(1L, request)
        );
    }

    @Test
    @WithMockUser(
            username = "user@example.com",
            roles = "USER"
    )
    void user_shouldNotDeleteUser() {

        assertThrows(
                AccessDeniedException.class,
                () -> userController.deleteUser(1L)
        );
    }

    @Test
    @WithMockUser(
            username = "admin@example.com",
            roles = "ADMIN"
    )
    void admin_shouldDeleteUser() {

        doNothing()
                .when(userService)
                .deleteUser(1L);

        assertDoesNotThrow(
                () -> userController.deleteUser(1L)
        );
    }
}