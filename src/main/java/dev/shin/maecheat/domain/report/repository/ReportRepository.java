package dev.shin.maecheat.domain.report.repository;

import dev.shin.maecheat.domain.report.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsBySourceUrl(String sourceUrl);
}
