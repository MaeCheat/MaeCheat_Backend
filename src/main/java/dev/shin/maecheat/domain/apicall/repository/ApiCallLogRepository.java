package dev.shin.maecheat.domain.apicall.repository;

import dev.shin.maecheat.domain.apicall.model.ApiCallLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface ApiCallLogRepository extends JpaRepository<ApiCallLog, Long> {

    Optional<ApiCallLog> findByCallDate(LocalDate callDate);

    @Query("SELECT COALESCE(SUM(a.callCount), 0) FROM ApiCallLog a")
    long sumTotalCallCount();
}
