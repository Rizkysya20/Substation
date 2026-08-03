package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateReportRequest {
    private Long substationId;
    private String title;
    private String description;
    private String category;
    private LocalDate incidentDate;

}
