package com.barangay.bims.web;

import com.barangay.bims.domain.Role;
import com.barangay.bims.domain.User;
import com.barangay.bims.repo.UserRepository;
import com.barangay.bims.web.dto.ProfileDtos.UpdateProfileRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public User me(HttpSession session) {
        User user = currentUser(session);
        user.setPassword(null);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody UpdateProfileRequest req, HttpSession session) {
        User user = currentUser(session);
        if (user.getRole() != Role.RESIDENT) {
            user.setFullName(req.fullName());
        }
        user.setContactNumber(req.contactNumber());
        user.setAddress(req.address());
        User saved = userRepository.save(user);
        saved.setPassword(null);
        return saved;
    }

    @PostMapping(value = "/id-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public User uploadValidId(@RequestParam("file") MultipartFile file, HttpSession session) {
        User user = currentUser(session);
        if (user.getRole() == Role.RESIDENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Residents can only update phone number and address");
        }

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required");
        }

        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) {
            ext = original.substring(dot + 1).toLowerCase();
        }

        if (!(ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png") || ext.equals("pdf"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPG, PNG, or PDF allowed");
        }

        Path uploadDir = Path.of("uploads", "ids");
        try {
            Files.createDirectories(uploadDir);
            String name = "user_" + user.getId() + "_" + Instant.now().toEpochMilli() + "." + ext;
            Path dest = uploadDir.resolve(name);
            Files.write(dest, file.getBytes());

            user.setIdDocumentPath(dest.toString().replace("\\", "/"));
            user.setVerified(false);
            User saved = userRepository.save(user);
            saved.setPassword(null);
            return saved;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload failed");
        }
    }

    private User currentUser(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
        return userRepository.findById(((Number) userId).longValue())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session"));
    }
}

