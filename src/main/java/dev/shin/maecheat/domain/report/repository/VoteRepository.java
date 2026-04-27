package dev.shin.maecheat.domain.report.repository;

import dev.shin.maecheat.domain.report.model.Report;
import dev.shin.maecheat.domain.report.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByReportAndVoterIp(Report report, String voterIp);
}
