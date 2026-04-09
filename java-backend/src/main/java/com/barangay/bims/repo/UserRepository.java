package com.barangay.bims.repo;

import com.barangay.bims.domain.User;
import com.barangay.bims.domain.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    long countByRoleAndOfficialApprovedTrue(Role role);
    List<User> findByRoleAndOfficialApprovedFalseOrderByCreatedAtAsc(Role role);
    List<User> findByRoleOrderByCreatedAtAsc(Role role);
}

