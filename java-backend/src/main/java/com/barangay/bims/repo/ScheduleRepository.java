package com.barangay.bims.repo;

import com.barangay.bims.domain.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findTop10ByOrderByScheduledDateAsc();
}

