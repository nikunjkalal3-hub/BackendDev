package backend.backenddev.Controller;

import backend.backenddev.Service.AuthService;
import backend.backenddev.Service.JwtService;
import backend.backenddev.dto.LoginRequest;
import backend.backenddev.dto.LoginResponse;
import backend.backenddev.dto.RegisterRequest;
import backend.backenddev.dto.UserResponse;
import backend.backenddev.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_shouldCreateUserAndReturnUserResponse() {

        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("Password@123");

        User createdUser = new User(
                "Test User",
                "test@example.com",
                "HASHED_PASSWORD"
        );

        ResponseEntity<UserResponse> expectedResponse =
                ResponseEntity.ok(UserResponse.fromUser(createdUser));

        when(authService.register(any(User.class)))
                .thenReturn(createdUser);

        ResponseEntity<UserResponse> response =
                authController.register(request);

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

        verify(authService).register(any(User.class));
    }

    @Test
    void register_shouldPassCorrectUserToAuthService() {

        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("Password@123");

        User createdUser = new User(
                "Test User",
                "test@example.com",
                "HASHED_PASSWORD"
        );

        when(authService.register(any(User.class)))
                .thenReturn(createdUser);

        authController.register(request);

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(authService).register(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        assertEquals(
                "Test User",
                capturedUser.getName()
        );

        assertEquals(
                "test@example.com",
                capturedUser.getEmail()
        );

        assertEquals(
                "Password@123",
                capturedUser.getPassword()
        );
    }

    @Test
    void login_shouldAuthenticateAndReturnJwt() {

        LoginRequest request = new LoginRequest(
                "test@example.com",
                "Password@123"
        );

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenReturn(authentication);

        when(authentication.getName())
                .thenReturn("test@example.com");

        when(jwtService.generateToken("test@example.com"))
                .thenReturn("test-jwt-token");

        ResponseEntity<LoginResponse> response =
                authController.login(request);

        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(
                "test-jwt-token",
                response.getBody().getToken()
        );

        verify(authenticationManager)
                .authenticate(any(
                        UsernamePasswordAuthenticationToken.class
                ));

        verify(jwtService)
                .generateToken("test@example.com");
    }
}