package com.barangay.bims.config;

import com.barangay.bims.domain.Role;
import com.barangay.bims.domain.User;
import com.barangay.bims.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultOfficialSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-official.username:official}")
    private String defaultUsername;

    @Value("${app.default-official.password:official123}")
    private String defaultPassword;

    @Value("${app.default-official.full-name:Default Barangay Official}")
    private String defaultFullName;

    public DefaultOfficialSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        long approvedOfficialCount = userRepository.countByRoleAndOfficialApprovedTrue(Role.OFFICIAL);
        if (approvedOfficialCount > 0) {
            return;
        }

        User existingByUsername = userRepository.findByUsername(defaultUsername).orElse(null);
        if (existingByUsername != null) {
            if (existingByUsername.getRole() == Role.OFFICIAL && !existingByUsername.isOfficialApproved()) {
                existingByUsername.setOfficialApproved(true);
                userRepository.save(existingByUsername);
            }
            return;
        }

        User seeded = new User();
        seeded.setUsername(defaultUsername);
        seeded.setFullName(defaultFullName);
        seeded.setPassword(passwordEncoder.encode(defaultPassword));
        seeded.setRole(Role.OFFICIAL);
        seeded.setOfficialApproved(true);
        userRepository.save(seeded);
    }
}
