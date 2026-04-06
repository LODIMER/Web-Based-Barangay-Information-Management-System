package com.barangay.bims.repo;

import com.barangay.bims.domain.BlotterReport;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlotterReportRepository extends JpaRepository<BlotterReport, Long> {
    List<BlotterReport> findTop50ByOrderByCreatedAtDesc();

    @Query("""
        SELECT b FROM BlotterReport b
        WHERE (:status IS NULL OR b.status = :status)
          AND (
              :keyword IS NULL OR
              LOWER(b.type) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
              LOWER(b.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
              LOWER(b.details) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        ORDER BY b.createdAt DESC
        """)
    Page<BlotterReport> search(
        @Param("status") String status,
        @Param("keyword") String keyword,
        Pageable pageable
    );
}

