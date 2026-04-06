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
    public String form(HttpSession session, Model model, RedirectAttributes ra) {
        String redirect = SessionSupport.requireLogin(session, userRepository, ra);
        if (redirect != null) return redirect;
        User user = SessionSupport.currentUserOrNull(session, userRepository);
        SessionSupport.nav(model, user);
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
        req = ayudaRequestRepository.save(req);

        if ((user.getRole() == Role.OFFICIAL || user.getRole() == Role.ADMIN) && req.getPreferredDate() != null) {
            Schedule schedule = new Schedule();
            schedule.setAyudaRequest(req);
            schedule.setScheduledDate(req.getPreferredDate());
            scheduleRepository.save(schedule);
            ra.addFlashAttribute("success", "Ayuda saved and synced to schedule.");
        } else {
            ra.addFlashAttribute("success", "Ayuda request submitted.");
        }

        return "redirect:/ayuda/request";
    }
}

