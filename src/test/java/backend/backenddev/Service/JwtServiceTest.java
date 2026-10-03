package backend.backenddev.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private final String secret =
            "my-super-secret-key-that-is-at-least-32-bytes-long";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(secret, 3600000);
    }

    @Test
    void generateToken_shouldContainUsername() {

        String token =
                jwtService.generateToken("user@example.com");

        String username =
                jwtService.extractUsername(token);

        assertEquals("user@example.com", username);
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {

        String token =
                jwtService.generateToken("user@example.com");

        boolean result =
                jwtService.isTokenValid(
                        token,
                        "user@example.com"
                );

        assertTrue(result);
    }

    @Test
    void isTokenValid_shouldReturnFalseForWrongUsername() {

        String token =
                jwtService.generateToken("user@example.com");

        boolean result =
                jwtService.isTokenValid(
                        token,
                        "admin@example.com"
                );

        assertFalse(result);
    }

    @Test
    void isTokenValid_shouldReturnFalseForExpiredToken() {

        SecretKey key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        Date now = new Date();

        String expiredToken = Jwts.builder()
                .subject("user@example.com")
                .issuedAt(new Date(now.getTime() - 10000))
                .expiration(new Date(now.getTime() - 5000))
                .signWith(key)
                .compact();

        assertFalse(
                jwtService.isTokenValid(
                        expiredToken,
                        "user@example.com"
                )
        );
    }

    @Test
    void extractUsername_shouldRejectMalformedToken() {

        String malformedToken =
                "this.is.not.a.valid.jwt";

        assertThrows(
                Exception.class,
                () -> jwtService.extractUsername(malformedToken)
        );
    }

    @Test
    void isTokenValid_shouldReturnFalseForMalformedToken() {

        String malformedToken =
                "this.is.not.a.valid.jwt";

        assertFalse(
                jwtService.isTokenValid(
                        malformedToken,
                        "user@example.com"
                )
        );
    }
}