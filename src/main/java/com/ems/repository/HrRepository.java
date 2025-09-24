package com.ems.repository;

import com.ems.model.Hr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HrRepository extends JpaRepository<Hr, Long> {
    Optional<Hr> findByHrId(String hrId);
    Optional<Hr> findByUsername(String username);
    Optional<Hr> findByEmail(String email);
    List<Hr> findByStatus(String status);
}