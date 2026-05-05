package dev.shin.maecheat.domain.report.repository;

import dev.shin.maecheat.domain.character.model.MapleCharacter;
import dev.shin.maecheat.domain.report.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    // 하나의 게시글이 여러 캐릭터에 등록될 수 있도록 sourceUrl과 mapleCharacter 조합으로 중복 체크
    boolean existsBySourceUrlAndMapleCharacter(String sourceUrl, MapleCharacter mapleCharacter);
    List<Report> findByMapleCharacterIdOrderByUpvotesDesc(Long mapleCharacterId);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT r.mapleCharacter.id FROM Report r")
    List<Long> findDistinctCharacterIds();

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT r.mapleCharacter.id FROM Report r WHERE r.mapleCharacter.aiSummary IS NULL")
    List<Long> findDistinctCharacterIdsWithoutSummary();
}
