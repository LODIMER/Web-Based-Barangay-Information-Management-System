package com.barangay.bims.web.page;

import com.barangay.bims.domain.Role;
import com.barangay.bims.domain.User;
import com.barangay.bims.repo.AyudaRequestRepository;
import com.barangay.bims.repo.ScheduleRepository;
import com.barangay.bims.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DashboardPageController {
    private final UserRepository userRepository;
    private final AyudaRequestRepository ayudaRequestRepository;
    private final ScheduleRepository scheduleRepository;

    public DashboardPageController(
        UserRepository userRepository,
        AyudaRequestRepository ayudaRequestRepository,
        ScheduleRepository scheduleRepository
    ) {
        this.userRepository = userRepository;
        this.ayudaRequestRepository = ayudaRequestRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model, RedirectAttributes ra) {
        String redirect = SessionSupport.requireLogin(session, userRepository, ra);
        if (redirect != null) return redirect;

        User user = SessionSupport.currentUserOrNull(session, userRepository);
        if (user.getRole() == Role.RESIDENT) {
            return "redirect:/ayuda/request";
        }
        SessionSupport.nav(model, user);

        model.addAttribute("isResident", user.getRole() == Role.RESIDENT);
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("ayudaCount", ayudaRequestRepository.count());
        model.addAttribute("scheduleCount", scheduleRepository.count());
        model.addAttribute("upcoming", scheduleRepository.findTop10ByOrderByScheduledDateAsc());
        boolean canApproveOfficials = SessionSupport.isApprovedOfficial(user);
        model.addAttribute("canApproveOfficials", canApproveOfficials);
        model.addAttribute("pendingOfficials", canApproveOfficials
            ? userRepository.findByRoleAndOfficialApprovedFalseOrderByCreatedAtAsc(Role.OFFICIAL)
            : java.util.List.of());
        return "dashboard/index";
    }

    @PostMapping("/officials/{officialId}/approve")
    public String approveOfficial(@PathVariable Long officialId, HttpSession session, RedirectAttributes ra) {
        User approver = SessionSupport.currentUserOrNull(session, userRepository);
        if (approver == null) {
            ra.addFlashAttribute("error", "Please login first.");
            return "redirect:/login";
        }
        if (!SessionSupport.isApprovedOfficial(approver)) {
            ra.addFlashAttribute("error", "Only approved officials can approve official accounts.");
            return "redirect:/officials/approvals";
        }

        User target = userRepository.findById(officialId).orElse(null);
        if (target == null || target.getRole() != Role.OFFICIAL) {
            ra.addFlashAttribute("error", "Official account not found.");
            return "redirect:/officials/approvals";
        }
        if (approver.getId().equals(target.getId())) {
            ra.addFlashAttribute("error", "You cannot approve your own account.");
            return "redirect:/officials/approvals";
        }
        if (target.isOfficialApproved()) {
            ra.addFlashAttribute("success", "Official account is already approved.");
            return "redirect:/officials/approvals";
        }

        target.setOfficialApproved(true);
        userRepository.save(target);
        ra.addFlashAttribute("success", "Official account approved.");
        return "redirect:/officials/approvals";
    }
}

