package com.example.demo.repository;

import com.example.demo.entity.MaintenanceReport;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceReportRepository extends JpaRepository<MaintenanceReport, Long> {
    List<MaintenanceReport> findByUser(User user);

}
