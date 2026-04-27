package dev.shin.maecheat.domain.report.controller;

import dev.shin.maecheat.domain.report.model.Report;
import dev.shin.maecheat.domain.report.model.VoteType;
import dev.shin.maecheat.domain.report.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maple-characters/{nickname}/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    public record ReportCreateRequest(String sourceUrl) {}

    @GetMapping
    public List<Report> getReports(@PathVariable String nickname) {
        return reportService.getReports(nickname);
    }

    @PostMapping
    public Report createReport(
            @PathVariable String nickname,
            @RequestBody ReportCreateRequest request) {
        return reportService.createReport(nickname, request.sourceUrl());
    }

    @PostMapping("/{reportId}/upvote")
    public Report upvote(@PathVariable String nickname, @PathVariable Long reportId, HttpServletRequest request) {
        return reportService.vote(reportId, getClientIp(request), VoteType.UP);
    }

    @PostMapping("/{reportId}/downvote")
    public Report downvote(@PathVariable String nickname, @PathVariable Long reportId, HttpServletRequest request) {
        return reportService.vote(reportId, getClientIp(request), VoteType.DOWN);
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
