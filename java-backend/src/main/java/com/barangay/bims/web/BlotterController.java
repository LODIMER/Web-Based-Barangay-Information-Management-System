package com.barangay.bims.web;

import com.barangay.bims.domain.BlotterReport;
import com.barangay.bims.domain.Role;
import com.barangay.bims.domain.User;
import com.barangay.bims.repo.BlotterReportRepository;
import com.barangay.bims.repo.UserRepository;
import com.barangay.bims.web.dto.BlotterDtos.CreateBlotterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/blotter")
public class BlotterController {
    private final BlotterReportRepository blotterRepo;
    private final UserRepository userRepo;

    public BlotterController(BlotterReportRepository blotterRepo, UserRepository userRepo) {
        this.blotterRepo = blotterRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<BlotterReport> list(HttpSession session) {
        requireLogin(session);
        return blotterRepo.findTop50ByOrderByCreatedAtDesc();
    }

    @PostMapping
    public BlotterReport create(@Valid @RequestBody CreateBlotterRequest req, HttpSession session) {
        User user = currentUser(session);
        BlotterReport report = new BlotterReport();
        report.setIncidentDate(req.incidentDate());
        report.setType(req.type());
        report.setLocation(req.location());
        report.setDetails(req.details());
        report.setReportedBy(user);
        report.setStatus("PENDING");
        return blotterRepo.save(report);
    }

    @PatchMapping("/{id}/status")
    public BlotterReport updateStatus(
        @PathVariable Long id,
        @RequestParam String status,
        HttpSession session
    ) {
        User user = currentUser(session);
        if (user.getRole() != Role.OFFICIAL && user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Official/admin access required");
        }

        String normalized = status == null ? "" : status.trim().toUpperCase();
        if (!normalized.equals("PENDING") && !normalized.equals("APPROVED")
            && !normalized.equals("RESOLVED") && !normalized.equals("REJECTED")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status");
        }

        BlotterReport report = blotterRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));
        report.setStatus(normalized);
        return blotterRepo.save(report);
    }

    private void requireLogin(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
    }

    private User currentUser(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
        return userRepo.findById(((Number) userId).longValue())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session"));
    }
}

