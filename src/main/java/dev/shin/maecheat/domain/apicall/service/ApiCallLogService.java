package dev.shin.maecheat.domain.apicall.service;

import dev.shin.maecheat.domain.apicall.model.ApiCallLog;
import dev.shin.maecheat.domain.apicall.repository.ApiCallLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ApiCallLogService {

    private final ApiCallLogRepository apiCallLogRepository;

    @Transactional
    public void incrementCount() {
        LocalDate today = LocalDate.now();
        ApiCallLog log = apiCallLogRepository.findByCallDate(today)
                .orElseGet(() -> apiCallLogRepository.save(
                        ApiCallLog.builder()
                                .callDate(today)
                                .callCount(0)
                                .build()
                ));
        log.incrementCount();
    }

    @Transactional(readOnly = true)
    public long getTotalCallCount() {
        return apiCallLogRepository.sumTotalCallCount();
    }
}
