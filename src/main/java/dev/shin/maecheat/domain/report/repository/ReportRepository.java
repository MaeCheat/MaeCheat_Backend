package dev.shin.maecheat.domain.report.repository;

import dev.shin.maecheat.domain.report.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsBySourceUrl(String sourceUrl);
    List<Report> findByMapleCharacterId(Long mapleCharacterId);
}
