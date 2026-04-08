package com.barangay.bims.web.page;

import com.barangay.bims.domain.AyudaRequest;
import com.barangay.bims.domain.Role;
import com.barangay.bims.domain.Schedule;
import com.barangay.bims.domain.UrgencyLevel;
import com.barangay.bims.domain.User;
import com.barangay.bims.repo.AyudaRequestRepository;
import com.barangay.bims.repo.ScheduleRepository;
import com.barangay.bims.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AyudaPageController {
    private final UserRepository userRepository;
    private final AyudaRequestRepository ayudaRequestRepository;
    private final ScheduleRepository scheduleRepository;

    public AyudaPageController(
        UserRepository userRepository,
        AyudaRequestRepository ayudaRequestRepository,
        ScheduleRepository scheduleRepository
    ) {
        this.userRepository = userRepository;
        this.ayudaRequestRepository = ayudaRequestRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/ayuda/request")
    public String form(
        @RequestParam(required = false) String status,
        HttpSession session,
        Model model,
        RedirectAttributes ra
    ) {
        String redirect = SessionSupport.requireLogin(session, userRepository, ra);
        if (redirect != null) return redirect;
        User user = SessionSupport.currentUserOrNull(session, userRepository);
        SessionSupport.nav(model, user);
        String selectedStatus = normalizeStatusFilter(status, user);
        if (selectedStatus == null) {
            model.addAttribute("requests", ayudaRequestRepository.findTop50ByOrderByCreatedAtDesc());
            model.addAttribute("selectedStatus", "ALL");
        } else {
            model.addAttribute("requests", ayudaRequestRepository.findTop50ByStatusOrderByCreatedAtDesc(selectedStatus));
            model.addAttribute("selectedStatus", selectedStatus);
        }
        return "ayuda/form";
    }

    @PostMapping("/ayuda/request")
    public String submit(
        @RequestParam String requestType,
        @RequestParam String urgencyLevel,
        @RequestParam String description,
        @RequestParam(required = false) String preferredDate,
        HttpSession session,
        RedirectAttributes ra
    ) {
        User user = SessionSupport.currentUserOrNull(session, userRepository);
        if (user == null) return "redirect:/login";

        AyudaRequest req = new AyudaRequest();
        req.setRequestType(requestType);
        req.setDescription(description);
        req.setUrgencyLevel(UrgencyLevel.valueOf(urgencyLevel.toUpperCase()));
        if (preferredDate != null && !preferredDate.isBlank()) {
            req.setPreferredDate(LocalDate.parse(preferredDate));
        }
        req.setCreatedBy(user);
        req.setStatus("PENDING");
        ayudaRequestRepository.save(req);

        ra.addFlashAttribute("success", "Ayuda request submitted and pending approval.");

        return "redirect:/ayuda/request";
    }

    @PostMapping("/ayuda/approve")
    public String approve(
        @RequestParam Long id,
        HttpSession session,
        RedirectAttributes ra
    ) {
        User user = SessionSupport.currentUserOrNull(session, userRepository);
        if (user == null) return "redirect:/login";
        if (user.getRole() != Role.OFFICIAL) {
            ra.addFlashAttribute("error", "Only barangay officials can approve ayuda requests.");
            return "redirect:/ayuda/request";
        }

        AyudaRequest req = ayudaRequestRepository.findById(id).orElse(null);
        if (req == null) {
            ra.addFlashAttribute("error", "Ayuda request not found.");
            return "redirect:/ayuda/request";
        }

        req.setStatus("APPROVED");
        ayudaRequestRepository.save(req);

        if (req.getPreferredDate() != null && !scheduleRepository.existsByAyudaRequestId(req.getId())) {
            Schedule schedule = new Schedule();
            schedule.setAyudaRequest(req);
            schedule.setScheduledDate(req.getPreferredDate());
            scheduleRepository.save(schedule);
        }

        ra.addFlashAttribute("success", "Ayuda request approved.");
        return "redirect:/ayuda/request";
    }

    private String normalizeStatusFilter(String status, User user) {
        // Officials default to pending queue for faster approval workflow.
        if (status == null || status.isBlank()) {
            return user != null && user.getRole() == Role.OFFICIAL ? "PENDING" : null;
        }
        String normalized = status.trim().toUpperCase();
        if (normalized.equals("ALL")) return null;
        if (normalized.equals("PENDING") || normalized.equals("APPROVED")) return normalized;
        return user != null && user.getRole() == Role.OFFICIAL ? "PENDING" : null;
    }
}

