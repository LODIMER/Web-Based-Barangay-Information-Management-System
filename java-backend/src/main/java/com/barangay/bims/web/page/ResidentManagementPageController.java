package com.barangay.bims.web.page;

import com.barangay.bims.domain.Role;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ResidentManagementPageController {
    private final UserRepository userRepository;

    public ResidentManagementPageController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/residents")
    public String residents(HttpSession session, Model model, RedirectAttributes ra) {
        String redirect = SessionSupport.requireLogin(session, userRepository, ra);
        if (redirect != null) return redirect;

        User user = SessionSupport.currentUserOrNull(session, userRepository);
        SessionSupport.nav(model, user);
        if (!SessionSupport.isApprovedOfficial(user)) {
            ra.addFlashAttribute("error", "Only approved officials can manage residents.");
            return "redirect:/";
        }

        model.addAttribute("residents", userRepository.findByRoleOrderByCreatedAtAsc(Role.RESIDENT));
        return "residents/index";
    }

    @PostMapping("/residents/{residentId}/update")
    public String updateResident(
        @PathVariable Long residentId,
        @RequestParam String fullName,
        @RequestParam(required = false) String contactNumber,
        @RequestParam(required = false) String address,
        @RequestParam(required = false) String residentNumber,
        @RequestParam(required = false, defaultValue = "false") boolean verified,
        @RequestParam(name = "idFile", required = false) MultipartFile idFile,
        HttpSession session,
        RedirectAttributes ra
    ) {
        User official = SessionSupport.currentUserOrNull(session, userRepository);
        if (!SessionSupport.isApprovedOfficial(official)) {
            ra.addFlashAttribute("error", "Only approved officials can manage residents.");
            return "redirect:/residents";
        }

        User resident = userRepository.findById(residentId).orElse(null);
        if (resident == null || resident.getRole() != Role.RESIDENT) {
            ra.addFlashAttribute("error", "Resident account not found.");
            return "redirect:/residents";
        }

        resident.setFullName(fullName);
        resident.setContactNumber(contactNumber);
        resident.setAddress(address);
        if (residentNumber != null && !residentNumber.isBlank()) {
            resident.setResidentNumber(residentNumber.trim());
        }
        resident.setVerified(verified);

        if (idFile != null && !idFile.isEmpty()) {
            String original = idFile.getOriginalFilename() == null ? "" : idFile.getOriginalFilename();
            String ext = "";
            int idx = original.lastIndexOf('.');
            if (idx >= 0) ext = original.substring(idx + 1).toLowerCase();
            boolean ok = ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png") || ext.equals("pdf");
            if (!ok) {
                ra.addFlashAttribute("error", "Valid ID must be JPG, PNG, or PDF.");
                return "redirect:/residents";
            }
            try {
                Path dir = Path.of("uploads", "ids");
                Files.createDirectories(dir);
                Path filePath = dir.resolve("resident_" + resident.getId() + "_" + Instant.now().toEpochMilli() + "." + ext);
                Files.write(filePath, idFile.getBytes());
                resident.setIdDocumentPath(filePath.toString().replace("\\", "/"));
            } catch (IOException e) {
                ra.addFlashAttribute("error", "Failed to upload resident ID.");
                return "redirect:/residents";
            }
        }

        userRepository.save(resident);
        ra.addFlashAttribute("success", "Resident profile updated.");
        return "redirect:/residents";
    }
}
