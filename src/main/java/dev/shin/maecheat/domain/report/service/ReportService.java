package dev.shin.maecheat.domain.report.service;

import dev.shin.maecheat.domain.character.model.MapleCharacter;
import dev.shin.maecheat.domain.character.repository.MapleCharacterRepository;
import dev.shin.maecheat.domain.report.model.Report;
import dev.shin.maecheat.domain.report.repository.ReportRepository;
import dev.shin.maecheat.infrastructure.scraper.WebScraper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
// 클래스 단위로 읽기 전용 트랜잭션을 적용하여 성능 최적화
// 쓰기 작업이 필요한 메서드에는 별도로 @Transactional을 적용하여 트랜잭션을 관리할 수 있도록 함
@Transactional(readOnly = true)
public class ReportService {
    private final WebScraper webScraper;
    private final ReportRepository reportRepository;
    private final MapleCharacterRepository mapleCharacterRepository;

    public List<Report> getReports(String nickname) {
        MapleCharacter character = mapleCharacterRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터입니다"));
        return reportRepository.findByMapleCharacterIdOrderByUpvotesDesc(character.getId());
    }

    @Transactional
    public Report upvote(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        report.upvote();
        return report;
    }

    @Transactional
    public Report downvote(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        report.downvote();
        return report;
    }

    @Transactional
    public Report createReport(String nickname, String sourceUrl) {
        MapleCharacter character = mapleCharacterRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터입니다"));

        if (reportRepository.existsBySourceUrlAndMapleCharacter(sourceUrl, character)) {
            throw new IllegalStateException("이미 등록된 URL입니다.");
        }

        WebScraper.ScrapedData scrapedData = webScraper.scrape(sourceUrl);

        String combined = scrapedData.title() + " " + scrapedData.content();
        if (!combined.contains(nickname)) {
            throw new IllegalArgumentException("게시글에 해당 캐릭터 닉네임이 포함되어 있지 않습니다. (캐릭터와 연관이 있는 게시물을 등록해 주세요)");
        }

        return reportRepository.save(
                Report.builder()
                        .mapleCharacter(character)
                        .sourceUrl(sourceUrl)
                        .title(scrapedData.title())
                        .content(scrapedData.content())
                        .build()
        );
    }
}
