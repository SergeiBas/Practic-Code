package org.example.practiccode.repository;

import org.example.practiccode.model.HRRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HRRequestRepository extends JpaRepository<HRRequest, Long> {
    List<HRRequest> findByEmployeeId(Long employeeId);
}