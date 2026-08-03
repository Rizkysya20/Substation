package com.example.demo.controller;

import com.example.demo.dto.CreateReportRequest;
import com.example.demo.entity.*;
import com.example.demo.repository.MaintenanceReportRepository;
import com.example.demo.repository.SubstationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class MaintenanceReportController {

    private final MaintenanceReportRepository reportRepository;
    private final SubstationRepository substationRepository;
    private final UserRepository userRepository;

    public MaintenanceReportController(MaintenanceReportRepository reportRepository,
            SubstationRepository substationRepository,
            UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.substationRepository = substationRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('TEKNISI')")
    public ResponseEntity<?> create(@RequestBody CreateReportRequest request, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!"));

        Substation substation = substationRepository.findById(request.getSubstationId())
                .orElseThrow(() -> new RuntimeException("Substation not found!"));

        MaintenanceReport report = new MaintenanceReport();
        report.setUser(user);
        report.setSubstation(substation);
        report.setTitle(request.getTitle());
        report.setDescription(request.getDescription());
        report.setCategory(ReportCategory.valueOf(request.getCategory()));
        report.setIncidentDate(request.getIncidentDate());
        report.setStatus(ReportStatus.PENDING);

        MaintenanceReport saved = reportRepository.save(report);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceReport>> getAll(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        boolean isTeknisi = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEKNISI"));

        if (isTeknisi) {
            return ResponseEntity.ok(reportRepository.findByUser(user));
        }

        return ResponseEntity.ok(reportRepository.findAll());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMIN')")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return reportRepository.findById(id)
                .map(report -> {
                    report.setStatus(ReportStatus.valueOf(body.get("status")));
                    MaintenanceReport updated = reportRepository.save(report);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
