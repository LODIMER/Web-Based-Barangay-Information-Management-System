package com.barangay.bims.web.page;

import com.barangay.bims.domain.User;
import com.barangay.bims.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfilePageController {
    private final UserRepository userRepository;

    public ProfilePageController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model, RedirectAttributes ra) {
        String redirect = SessionSupport.requireLogin(session, userRepository, ra);
        if (redirect != null) return redirect;
        User user = SessionSupport.currentUserOrNull(session, userRepository);
        SessionSupport.nav(model, user);
        model.addAttribute("userProfile", user);
        return "profile/index";
    }

    @PostMapping("/profile")
    public String update(
        @RequestParam String fullName,
        @RequestParam(required = false) String contactNumber,
        @RequestParam(required = false) String address,
        @RequestParam(name = "idFile", required = false) MultipartFile idFile,
        HttpSession session,
        RedirectAttributes ra
    ) {
        User user = SessionSupport.currentUserOrNull(session, userRepository);
        if (user == null) return "redirect:/login";

        user.setFullName(fullName);
        user.setContactNumber(contactNumber);
        user.setAddress(address);

        if (idFile != null && !idFile.isEmpty()) {
            String original = idFile.getOriginalFilename() == null ? "" : idFile.getOriginalFilename();
            String ext = "";
            int idx = original.lastIndexOf('.');
            if (idx >= 0) ext = original.substring(idx + 1).toLowerCase();
            boolean ok = ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png") || ext.equals("pdf");
            if (!ok) {
                ra.addFlashAttribute("error", "Valid ID must be JPG, PNG, or PDF.");
                return "redirect:/profile";
            }
            try {
                Path dir = Path.of("uploads", "ids");
                Files.createDirectories(dir);
                Path filePath = dir.resolve("user_" + user.getId() + "_" + Instant.now().toEpochMilli() + "." + ext);
                Files.write(filePath, idFile.getBytes());
                user.setIdDocumentPath(filePath.toString().replace("\\", "/"));
                user.setVerified(false);
            } catch (IOException e) {
                ra.addFlashAttribute("error", "Failed to upload ID.");
                return "redirect:/profile";
            }
        }

        userRepository.save(user);
        ra.addFlashAttribute("success", "Profile updated.");
        return "redirect:/profile";
    }
}

