package backend.backenddev;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

@SpringBootApplication
public class BackendDevApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(BackendDevApplication.class, args);
    }
    }

