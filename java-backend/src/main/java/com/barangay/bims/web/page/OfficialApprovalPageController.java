package com.barangay.bims.web.page;

import com.barangay.bims.domain.Role;
import com.barangay.bims.domain.User;
import com.barangay.bims.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OfficialApprovalPageController {
    private final UserRepository userRepository;

    public OfficialApprovalPageController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/officials/approvals")
    public String approvalsPage(HttpSession session, Model model, RedirectAttributes ra) {
        String redirect = SessionSupport.requireLogin(session, userRepository, ra);
        if (redirect != null) return redirect;

        User user = SessionSupport.currentUserOrNull(session, userRepository);
        SessionSupport.nav(model, user);

        if (!SessionSupport.isApprovedOfficial(user)) {
            ra.addFlashAttribute("error", "Only approved officials can access official approvals.");
            return "redirect:/";
        }

        model.addAttribute("pendingOfficials", userRepository.findByRoleAndOfficialApprovedFalseOrderByCreatedAtAsc(Role.OFFICIAL));
        return "officials/approvals";
    }
}
