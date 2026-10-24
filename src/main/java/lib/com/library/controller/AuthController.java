package lib.com.library.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lib.com.library.Auth.JwtUtil;
import lib.com.library.model.UserModel;
import lib.com.library.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String identifier = request.username() != null && !request.username().isBlank()
                ? request.username().trim()
                : (request.email() != null ? request.email().trim() : null);

        if (identifier == null || request.password() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Username/email and password are required"));
        }

        System.out.println("[AUTH] Received login request for identifier: '" + identifier + "'");

        Optional<UserModel> userOpt = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByName(identifier));

        if (userOpt.isEmpty()) {
            long count = userRepository.count();
            System.out.println(
                    "[AUTH] No user found in 'users' table for '" + identifier + "'. Total users in DB: " + count);
            HashMap<String, Object> response = new HashMap<>();
            response.put("error", "user not found");
            response.put("code", 404);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

        UserModel user = userOpt.get();
        System.out.println("[AUTH] User found: ID=" + user.getId() + ", Name='" + user.getName() + "', Email='"
                + user.getEmail() + "'");

        String storedHash = user.getPassword_hash();
        boolean isPasswordValid = false;

        if (storedHash != null) {
            if (passwordEncoder.matches(request.password(), storedHash)) {
                isPasswordValid = true;
            }
            else if (request.password().equals(storedHash) || request.password().trim().equals(storedHash.trim())) {
                isPasswordValid = true;
            }
        }

        if (!isPasswordValid) {
            System.out.println("[AUTH] Password did not match for user '" + identifier + "'");
            HashMap<String, Object> response = new HashMap<>();
            response.put("error", "Invalid username or password");
            response.put("code", 401);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

        System.out.println("[AUTH] Login successful for user: " + user.getEmail());

        String token = jwtUtil.generateToken(
                user.getEmail(),
                Map.of("role", user.getRole(), "name", user.getName()));
                HashMap<String,Object> response = new HashMap<>();
                response.put("token", token);
                response.put("code", 200);
                response.put("type", "Bearer");
                response.put("user", Map.of(
                  
                    "name", user.getName(),
                      "email",user.getEmail()
                  
                ));
                return ResponseEntity.ok(response);

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.email() == null || request.password() == null || request.name() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Name, email, and password are required"));
        }

        if (userRepository.existsByEmail(request.email().trim())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email is already registered"));
        }

        UserModel newUser = UserModel.builder()
                .name(request.name().trim())
                .email(request.email().trim())
                .password_hash(passwordEncoder.encode(request.password()))
                .role(request.role() != null && !request.role().isBlank() ? request.role() : "ROLE_USER")
                .build();

        UserModel savedUser = userRepository.save(newUser);
        System.out.println("[AUTH] Registered new user: " + savedUser.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "User registered successfully",
                "user", Map.of(
                        "id", savedUser.getId(),
                        "name", savedUser.getName(),
                        "email", savedUser.getEmail(),
                        "role", savedUser.getRole())));
    }
}

record LoginRequest(String username, String email, String password) {
}

record RegisterRequest(String name, String email, String password, String role) {
}