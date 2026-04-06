package com.barangay.bims.web.page;

import com.barangay.bims.domain.User;
import com.barangay.bims.repo.ScheduleRepository;
import com.barangay.bims.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SchedulePageController {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public SchedulePageController(UserRepository userRepository, ScheduleRepository scheduleRepository) {
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/schedule")
    public String index(HttpSession session, Model model, RedirectAttributes ra) {
        String redirect = SessionSupport.requireLogin(session, userRepository, ra);
        if (redirect != null) return redirect;
        User user = SessionSupport.currentUserOrNull(session, userRepository);
        SessionSupport.nav(model, user);
        model.addAttribute("upcoming", scheduleRepository.findTop10ByOrderByScheduledDateAsc());
        return "schedule/index";
    }
}

