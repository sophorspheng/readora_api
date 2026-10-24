package lib.com.library.implement;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lib.com.library.Auth.JwtUtil;
import lib.com.library.model.BookModel;
import lib.com.library.model.UserModel;
import lib.com.library.repository.UserRepository;
import lib.com.library.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Map<String, Object> login(String loginIdentifier, String password) {
        if (loginIdentifier == null || loginIdentifier.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Email and password cannot be empty");
        }

        UserModel user = userRepository.findByEmail(loginIdentifier.trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Match against BCrypt hashed password (with fallback to direct comparison for unhashed legacy records)
        boolean matches = passwordEncoder.matches(password, user.getPassword_hash())
                || password.equals(user.getPassword_hash());

        if (!matches) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                Map.of("role", user.getRole(), "name", user.getName())
        );

        return Map.of(
                "token", token,
                "type", "Bearer",
                "user", Map.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "role", user.getRole()
                )
        );
    }

    @Override
    public UserModel register(String name, String email, String password, String role) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        String trimmedEmail = email.trim();
        if (userRepository.existsByEmail(trimmedEmail)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        String encodedPassword = passwordEncoder.encode(password);
        String userRole = (role != null && !role.isBlank()) ? role : "ROLE_USER";

        UserModel newUser = UserModel.builder()
                .name(name.trim())
                .email(trimmedEmail)
                .password_hash(encodedPassword)
                .role(userRole)
                .build();

        return userRepository.save(newUser);
    }

}
