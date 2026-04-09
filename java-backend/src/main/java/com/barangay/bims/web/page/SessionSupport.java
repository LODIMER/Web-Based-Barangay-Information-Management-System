package com.barangay.bims.web.page;

import com.barangay.bims.domain.User;
import com.barangay.bims.domain.Role;
import com.barangay.bims.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public final class SessionSupport {
    private SessionSupport() {}

    public static User currentUserOrNull(HttpSession session, UserRepository userRepository) {
        Object userId = session.getAttribute("userId");
        if (userId == null) return null;
        return userRepository.findById(((Number) userId).longValue()).orElse(null);
    }

    public static String requireLogin(HttpSession session, UserRepository userRepository, RedirectAttributes ra) {
        if (currentUserOrNull(session, userRepository) == null) {
            ra.addFlashAttribute("error", "Please login first.");
            return "redirect:/login";
        }
        return null;
    }

    public static void nav(Model model, User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("role", user == null ? "GUEST" : user.getRole().name());
        model.addAttribute("isOfficialOrAdmin", user != null && (user.getRole() == Role.OFFICIAL || user.getRole() == Role.ADMIN));
        model.addAttribute("canApproveOfficials", isApprovedOfficial(user));
    }

    public static boolean isOfficialOrAdmin(User user) {
        return user != null && (user.getRole() == Role.OFFICIAL || user.getRole() == Role.ADMIN);
    }

    public static boolean isApprovedOfficial(User user) {
        return user != null && user.getRole() == Role.OFFICIAL && user.isOfficialApproved();
    }
}

