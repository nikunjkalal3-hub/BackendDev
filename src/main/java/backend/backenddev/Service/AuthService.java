package backend.backenddev.Service;


import backend.backenddev.entity.User;
import backend.backenddev.exception.EmailAlreadyExistsException;
import backend.backenddev.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder){
         this.userRepository=userRepository;
         this.passwordEncoder=passwordEncoder;
    }
    public User register(User user){

        if(userRepository.existsByEmail(user.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists: "+user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

}
