package dev.shin.maecheat.domain.report.controller;

import dev.shin.maecheat.domain.report.model.Report;
import dev.shin.maecheat.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/maple-characters/{mapleCharacterId}/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    public record ReportCreateRequest(String sourceUrl) {}

    @PostMapping
    public Report createReport(
            @PathVariable Long mapleCharacterId,
            @RequestBody ReportCreateRequest request) {
        return reportService.createReport(mapleCharacterId, request.sourceUrl());
    }
}
