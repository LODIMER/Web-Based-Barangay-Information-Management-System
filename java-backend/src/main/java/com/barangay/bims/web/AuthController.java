package com.barangay.bims.web;

import com.barangay.bims.domain.Role;
import com.barangay.bims.domain.User;
import com.barangay.bims.repo.UserRepository;
import com.barangay.bims.web.dto.AuthDtos.AuthResponse;
import com.barangay.bims.web.dto.AuthDtos.LoginRequest;
import com.barangay.bims.web.dto.AuthDtos.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        Role selectedRole = normalizeRole(req.role());
        if (selectedRole == Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin role is not self-registrable");
        }
        return registerWithRole(req, selectedRole);
    }

    @PostMapping("/register/resident")
    public AuthResponse registerResident(@Valid @RequestBody RegisterRequest req) {
        RegisterRequest normalizedReq = new RegisterRequest(
            req.username(), req.fullName(), req.password(), req.confirmPassword(), "RESIDENT"
        );
        return register(normalizedReq);
    }

    @PostMapping("/register/official")
    public AuthResponse registerOfficial(@Valid @RequestBody RegisterRequest req) {
        RegisterRequest normalizedReq = new RegisterRequest(
            req.username(), req.fullName(), req.password(), req.confirmPassword(), "OFFICIAL"
        );
        return register(normalizedReq);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req, HttpSession session) {
        Role selectedRole = normalizeRole(req.role());
        User user = userRepository.findByUsername(req.username())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        if (!roleMatchesSelection(user.getRole(), selectedRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role does not match selected account type");
        }

        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole().name());

        return new AuthResponse(user.getId(), user.getUsername(), user.getRole().name());
    }

    @PostMapping("/login/official")
    public AuthResponse officialLogin(@Valid @RequestBody LoginRequest req, HttpSession session) {
        LoginRequest normalizedReq = new LoginRequest(req.username(), req.password(), "OFFICIAL");
        return login(normalizedReq, session);
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    private AuthResponse registerWithRole(RegisterRequest req, Role role) {
        if (!req.password().equals(req.confirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        }
        if (userRepository.findByUsername(req.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already taken");
        }

        User user = new User();
        user.setUsername(req.username());
        user.setFullName(req.fullName());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole(role);

        User saved = userRepository.save(user);

        // Stable resident number: derive from DB id (avoids duplicates if rows are deleted).
        if (role == Role.RESIDENT && (saved.getResidentNumber() == null || saved.getResidentNumber().isBlank())) {
            saved.setResidentNumber(String.format("RES-%03d", saved.getId()));
            saved = userRepository.save(saved);
        }
        return new AuthResponse(saved.getId(), saved.getUsername(), saved.getRole().name());
    }

    private Role normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is required");
        }
        String normalized = role.trim().toUpperCase();
        try {
            return Role.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role selected");
        }
    }

    private boolean roleMatchesSelection(Role actualRole, Role selectedRole) {
        if (selectedRole == Role.RESIDENT) return actualRole == Role.RESIDENT;
        if (selectedRole == Role.OFFICIAL) return actualRole == Role.OFFICIAL || actualRole == Role.ADMIN;
        return false;
    }
}

