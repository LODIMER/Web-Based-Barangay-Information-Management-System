package com.barangay.bims.web;

import com.barangay.bims.domain.AyudaRequest;
import com.barangay.bims.domain.Role;
import com.barangay.bims.domain.Schedule;
import com.barangay.bims.domain.User;
import com.barangay.bims.repo.AyudaRequestRepository;
import com.barangay.bims.repo.ScheduleRepository;
import com.barangay.bims.repo.UserRepository;
import com.barangay.bims.web.dto.AyudaDtos.CreateAyudaRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ayuda")
public class AyudaController {
    private final AyudaRequestRepository ayudaRequestRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public AyudaController(
        AyudaRequestRepository ayudaRequestRepository,
        ScheduleRepository scheduleRepository,
        UserRepository userRepository
    ) {
        this.ayudaRequestRepository = ayudaRequestRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public AyudaRequest create(@Valid @RequestBody CreateAyudaRequest req, HttpSession session) {
        User user = currentUser(session);

        AyudaRequest item = new AyudaRequest();
        item.setRequestType(req.requestType());
        item.setUrgencyLevel(req.urgencyLevel());
        item.setDescription(req.description());
        item.setPreferredDate(req.preferredDate());
        item.setCreatedBy(user);

        AyudaRequest saved = ayudaRequestRepository.save(item);

        if ((user.getRole() == Role.OFFICIAL || user.getRole() == Role.ADMIN) && req.preferredDate() != null) {
            Schedule schedule = new Schedule();
            schedule.setAyudaRequest(saved);
            schedule.setScheduledDate(req.preferredDate());
            scheduleRepository.save(schedule);
        }

        return saved;
    }

    private User currentUser(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
        return userRepository.findById(((Number) userId).longValue())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session"));
    }
}

