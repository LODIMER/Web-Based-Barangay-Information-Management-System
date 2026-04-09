package com.barangay.bims.web.page;

import com.barangay.bims.domain.Role;
import com.barangay.bims.domain.User;
import com.barangay.bims.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthPageController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthPageController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("selectedRole", "RESIDENT");
        return "auth/login";
    }

    @GetMapping("/login/official")
    public String officialLoginPage() {
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(
        @RequestParam String username,
        @RequestParam String password,
        @RequestParam(defaultValue = "RESIDENT") String role,
        HttpSession session,
        RedirectAttributes ra
    ) {
        String selectedRole = normalizeRoleSelection(role);
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            ra.addFlashAttribute("error", "Invalid credentials.");
            return "redirect:/login";
        }
        if (!loginRoleMatches(user.getRole(), selectedRole)) {
            ra.addFlashAttribute("error", "Account type does not match your selection.");
            return "redirect:/login";
        }
        if (user.getRole() == Role.OFFICIAL && !user.isOfficialApproved()) {
            ra.addFlashAttribute("error", "Official account is pending approval by an approved official.");
            return "redirect:/login";
        }

        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole().name());
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("selectedRole", "RESIDENT");
        return "auth/register";
    }

    @GetMapping("/register/official")
    public String officialRegisterPage() {
        return "redirect:/register";
    }

    @PostMapping("/register")
    public String register(
        @RequestParam String username,
        @RequestParam String fullName,
        @RequestParam String password,
        @RequestParam String confirmPassword,
        @RequestParam(defaultValue = "RESIDENT") String role,
        RedirectAttributes ra
    ) {
        String selectedRole = normalizeRoleSelection(role);
        if (!password.equals(confirmPassword)) {
            ra.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/register";
        }
        if (password.length() < 6) {
            ra.addFlashAttribute("error", "Password must be at least 6 characters.");
            return "redirect:/register";
        }
        if (userRepository.findByUsername(username).isPresent()) {
            ra.addFlashAttribute("error", "Username already exists.");
            return "redirect:/register";
        }

        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("OFFICIAL".equals(selectedRole) ? Role.OFFICIAL : Role.RESIDENT);
        user.setOfficialApproved(!"OFFICIAL".equals(selectedRole));
        user = userRepository.save(user);

        if ("RESIDENT".equals(selectedRole)) {
            user.setResidentNumber(String.format("RES-%03d", user.getId()));
            userRepository.save(user);
        }

        if ("OFFICIAL".equals(selectedRole)) {
            ra.addFlashAttribute("success", "Official account created and pending approval by an approved official.");
        } else {
            ra.addFlashAttribute("success", "Account created. Please login.");
        }
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private String normalizeRoleSelection(String role) {
        String normalized = role == null ? "" : role.trim().toUpperCase();
        if (!normalized.equals("RESIDENT") && !normalized.equals("OFFICIAL")) {
            return "RESIDENT";
        }
        return normalized;
    }

    private boolean loginRoleMatches(Role actualRole, String selectedRole) {
        if ("RESIDENT".equals(selectedRole)) return actualRole == Role.RESIDENT;
        return actualRole == Role.OFFICIAL || actualRole == Role.ADMIN;
    }
}

