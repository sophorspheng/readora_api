package lib.com.library.helper;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lib.com.library.model.UserModel;
import lib.com.library.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            System.out.println("========== [DATA INITIALIZER] ==========");
            long count = userRepository.count();
            System.out.println("Total users found in database: " + count);

            boolean adminExists = userRepository.findByName("admin").isPresent()
                    || userRepository.findByEmail("admin@library.com").isPresent()
                    || userRepository.findByEmail("admin@gmail.com").isPresent()
                    || userRepository.findByEmail("admin").isPresent();

            if (!adminExists) {
                UserModel admin = UserModel.builder()
                        .name("admin")
                        .email("admin@library.com")
                        .password_hash(passwordEncoder.encode("admin"))
                        .role("ROLE_ADMIN")
                        .build();

                userRepository.save(admin);
                System.out.println(">>> Created default admin user: username/name='admin', email='admin@library.com', password='admin', role='ROLE_ADMIN'");
            } else {
                System.out.println(">>> Admin user already exists in database.");
            }

            List<UserModel> users = userRepository.findAll();
            for (UserModel u : users) {
                System.out.println(" - User [ID: " + u.getId() + ", Name: '" + u.getName() + "', Email: '" + u.getEmail() + "', Role: '" + u.getRole() + "']");
            }
            System.out.println("========================================");
        } catch (Exception e) {
            System.err.println("[DATA INITIALIZER] Warning: Could not initialize/query users table: " + e.getMessage());
        }
    }
}
