package com.barangay.bims.web.page;

import com.barangay.bims.domain.BlotterReport;
import com.barangay.bims.domain.User;
import com.barangay.bims.repo.BlotterReportRepository;
import com.barangay.bims.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BlotterPageController {
    private final UserRepository userRepository;
    private final BlotterReportRepository blotterRepo;

    public BlotterPageController(UserRepository userRepository, BlotterReportRepository blotterRepo) {
        this.userRepository = userRepository;
        this.blotterRepo = blotterRepo;
    }

    @GetMapping("/blotter")
    public String index(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String q,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        HttpSession session,
        Model model,
        RedirectAttributes ra
    ) {
        String redirect = SessionSupport.requireLogin(session, userRepository, ra);
        if (redirect != null) return redirect;

        User user = SessionSupport.currentUserOrNull(session, userRepository);
        SessionSupport.nav(model, user);
        String normalizedStatus = normalizeStatus(status);
        String keyword = (q == null || q.isBlank()) ? null : q.trim();
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 5), 50);
        Page<BlotterReport> reportPage = blotterRepo.search(
            normalizedStatus,
            keyword,
            PageRequest.of(safePage, safeSize)
        );

        model.addAttribute("selectedStatus", normalizedStatus == null ? "ALL" : normalizedStatus);
        model.addAttribute("query", q == null ? "" : q);
        model.addAttribute("reports", reportPage.getContent());
        model.addAttribute("page", safePage);
        model.addAttribute("size", safeSize);
        model.addAttribute("hasPrev", reportPage.hasPrevious());
        model.addAttribute("hasNext", reportPage.hasNext());
        model.addAttribute("totalPages", reportPage.getTotalPages());
        return "blotter/index";
    }

    @PostMapping("/blotter")
    public String create(
        @RequestParam String incidentDate,
        @RequestParam String type,
        @RequestParam String location,
        @RequestParam String details,
        HttpSession session,
        RedirectAttributes ra
    ) {
        User user = SessionSupport.currentUserOrNull(session, userRepository);
        if (user == null) return "redirect:/login";

        if (type.isBlank() || location.isBlank() || details.isBlank()) {
            ra.addFlashAttribute("error", "Please fill in all required fields.");
            return "redirect:/blotter";
        }

        BlotterReport report = new BlotterReport();
        report.setIncidentDate(LocalDate.parse(incidentDate));
        report.setType(type);
        report.setLocation(location);
        report.setDetails(details);
        report.setReportedBy(user);
        report.setStatus("PENDING");
        blotterRepo.save(report);

        ra.addFlashAttribute("success", "Blotter report submitted.");
        return "redirect:/blotter";
    }

    @PostMapping("/blotter/status")
    public String updateStatus(
        @RequestParam Long id,
        @RequestParam String status,
        HttpSession session,
        RedirectAttributes ra
    ) {
        User user = SessionSupport.currentUserOrNull(session, userRepository);
        if (!SessionSupport.isOfficialOrAdmin(user)) {
            ra.addFlashAttribute("error", "Only officials/admin can update status.");
            return "redirect:/blotter";
        }

        String normalized = status == null ? "" : status.trim().toUpperCase();
        if (!normalized.equals("PENDING") && !normalized.equals("APPROVED")
            && !normalized.equals("RESOLVED") && !normalized.equals("REJECTED")) {
            ra.addFlashAttribute("error", "Invalid status selected.");
            return "redirect:/blotter";
        }

        BlotterReport report = blotterRepo.findById(id).orElse(null);
        if (report == null) {
            ra.addFlashAttribute("error", "Report not found.");
            return "redirect:/blotter";
        }

        report.setStatus(normalized);
        blotterRepo.save(report);
        ra.addFlashAttribute("success", "Blotter status updated.");
        return "redirect:/blotter";
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank() || status.equalsIgnoreCase("ALL")) {
            return null;
        }
        String normalized = status.trim().toUpperCase();
        if (normalized.equals("PENDING") || normalized.equals("APPROVED")
            || normalized.equals("RESOLVED") || normalized.equals("REJECTED")) {
            return normalized;
        }
        return null;
    }
}

