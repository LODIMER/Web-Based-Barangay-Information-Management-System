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
        model.addAttribute("official", false);
        return "auth/login";
    }

    @GetMapping("/login/official")
    public String officialLoginPage(Model model) {
        model.addAttribute("official", true);
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(
        @RequestParam String username,
        @RequestParam String password,
        @RequestParam(defaultValue = "false") boolean official,
        HttpSession session,
        RedirectAttributes ra
    ) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            ra.addFlashAttribute("error", "Invalid credentials.");
            return official ? "redirect:/login/official" : "redirect:/login";
        }
        if (official && user.getRole() != Role.OFFICIAL && user.getRole() != Role.ADMIN) {
            ra.addFlashAttribute("error", "Official account required.");
            return "redirect:/login/official";
        }

        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole().name());
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("official", false);
        return "auth/register";
    }

    @GetMapping("/register/official")
    public String officialRegisterPage(Model model) {
        model.addAttribute("official", true);
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
        @RequestParam String username,
        @RequestParam String fullName,
        @RequestParam String password,
        @RequestParam String confirmPassword,
        @RequestParam(defaultValue = "false") boolean official,
        RedirectAttributes ra
    ) {
        if (!password.equals(confirmPassword)) {
            ra.addFlashAttribute("error", "Passwords do not match.");
            return official ? "redirect:/register/official" : "redirect:/register";
        }
        if (password.length() < 6) {
            ra.addFlashAttribute("error", "Password must be at least 6 characters.");
            return official ? "redirect:/register/official" : "redirect:/register";
        }
        if (userRepository.findByUsername(username).isPresent()) {
            ra.addFlashAttribute("error", "Username already exists.");
            return official ? "redirect:/register/official" : "redirect:/register";
        }

        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(official ? Role.OFFICIAL : Role.RESIDENT);
        user = userRepository.save(user);

        if (!official) {
            user.setResidentNumber(String.format("RES-%03d", user.getId()));
            userRepository.save(user);
        }

        ra.addFlashAttribute("success", "Account created. Please login.");
        return official ? "redirect:/login/official" : "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

