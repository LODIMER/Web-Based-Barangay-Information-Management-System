package com.barangay.bims.web;

import com.barangay.bims.domain.Role;
import com.barangay.bims.repo.ScheduleRepository;
import com.barangay.bims.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleController(ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/upcoming")
    public Object upcoming(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (!(userId instanceof Number numberUserId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
        var user = userRepository.findById(numberUserId.longValue())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session"));
        if (user.getRole() == Role.OFFICIAL && user.isOfficialApproved()) {
            return scheduleRepository.findTop10ByOrderByScheduledDateAsc();
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Approved official access required");
    }
}

