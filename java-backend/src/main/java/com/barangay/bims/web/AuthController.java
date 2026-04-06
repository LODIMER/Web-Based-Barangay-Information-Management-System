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

    @PostMapping("/register/resident")
    public AuthResponse registerResident(@Valid @RequestBody RegisterRequest req) {
        return register(req, Role.RESIDENT);
    }

    @PostMapping("/register/official")
    public AuthResponse registerOfficial(@Valid @RequestBody RegisterRequest req) {
        return register(req, Role.OFFICIAL);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req, HttpSession session) {
        User user = userRepository.findByUsername(req.username())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole().name());

        return new AuthResponse(user.getId(), user.getUsername(), user.getRole().name());
    }

    @PostMapping("/login/official")
    public AuthResponse officialLogin(@Valid @RequestBody LoginRequest req, HttpSession session) {
        AuthResponse resp = login(req, session);
        if (!"OFFICIAL".equals(resp.role()) && !"ADMIN".equals(resp.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Official account required");
        }
        return resp;
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    private AuthResponse register(RegisterRequest req, Role role) {
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
}

