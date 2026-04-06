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
        SessionSupport.nav(model, user);

        model.addAttribute("isResident", user.getRole() == Role.RESIDENT);
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("ayudaCount", ayudaRequestRepository.count());
        model.addAttribute("scheduleCount", scheduleRepository.count());
        model.addAttribute("upcoming", scheduleRepository.findTop10ByOrderByScheduledDateAsc());
        return "dashboard/index";
    }
}

