package com.example.demo.repository;


import com.example.demo.entity.audit.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<Audit, Long> {
}