package com.barangay.bims.repo;

import com.barangay.bims.domain.AyudaRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AyudaRequestRepository extends JpaRepository<AyudaRequest, Long> {
    List<AyudaRequest> findTop50ByOrderByCreatedAtDesc();
    List<AyudaRequest> findTop50ByStatusOrderByCreatedAtDesc(String status);
}

