package dev.shin.maecheat.domain.apicall.controller;

import dev.shin.maecheat.domain.apicall.service.ApiCallLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-call-log")
@RequiredArgsConstructor
public class ApiCallLogController {

    private final ApiCallLogService apiCallLogService;

    @GetMapping("/total")
    public long getTotalCallCount() {
        return apiCallLogService.getTotalCallCount();
    }
}
