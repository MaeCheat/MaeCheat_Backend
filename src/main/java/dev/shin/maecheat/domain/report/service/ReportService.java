package dev.shin.maecheat.domain.report.service;

import dev.shin.maecheat.domain.character.model.MapleCharacter;
import dev.shin.maecheat.domain.character.repository.MapleCharacterRepository;
import dev.shin.maecheat.domain.report.model.Report;
import dev.shin.maecheat.domain.report.model.Vote;
import dev.shin.maecheat.domain.report.model.VoteType;
import dev.shin.maecheat.domain.report.repository.ReportRepository;
import dev.shin.maecheat.domain.report.repository.VoteRepository;
import dev.shin.maecheat.infrastructure.ai.AiSummaryClient;
import dev.shin.maecheat.infrastructure.scraper.WebScraper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
// 클래스 단위로 읽기 전용 트랜잭션을 적용하여 성능 최적화
// 쓰기 작업이 필요한 메서드에는 별도로 @Transactional을 적용하여 트랜잭션을 관리할 수 있도록 함
@Transactional(readOnly = true)
public class ReportService {
    private final WebScraper webScraper;
    private final AiSummaryClient aiSummaryClient;
    private final ReportRepository reportRepository;
    private final VoteRepository voteRepository;
    private final MapleCharacterRepository mapleCharacterRepository;

    public List<Report> getReports(String nickname) {
        MapleCharacter character = mapleCharacterRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터입니다"));
        return reportRepository.findByMapleCharacterIdOrderByUpvotesDesc(character.getId());
    }

    @Transactional
    public Report vote(Long reportId, String voterIp, VoteType voteType) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Optional<Vote> existingVote = voteRepository.findByReportAndVoterIp(report, voterIp);

        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();

            if (vote.getVoteType() == voteType) {
                // 같은 버튼 다시 누름 → 투표 취소
                voteRepository.delete(vote);
                if (voteType == VoteType.UP) report.cancelUpvote();
                else report.cancelDownvote();
            } else {
                // 반대 버튼 누름 → 전환
                if (vote.getVoteType() == VoteType.UP) {
                    report.cancelUpvote();
                    report.downvote();
                } else {
                    report.cancelDownvote();
                    report.upvote();
                }
                vote.changeVoteType(voteType);
            }
        } else {
            // 첫 투표
            voteRepository.save(Vote.builder()
                    .report(report)
                    .voterIp(voterIp)
                    .voteType(voteType)
                    .build());
            if (voteType == VoteType.UP) report.upvote();
            else report.downvote();
        }

        return report;
    }

    private void validatePostUrl(String url) {
        try {
            URI uri = URI.create(url);
            String host = uri.getHost();
            String path = uri.getPath();

            // 인벤: /board/{game}/{boardId}/{postId} 형태여야 함
            if (host != null && host.contains("inven.co.kr")) {
                String[] segments = path.split("/");
                // ["", "board", "maple", "5974", "6523271"] → 5개
                if (segments.length < 5 || !segments[segments.length - 1].matches("\\d+")) {
                    throw new IllegalArgumentException("게시판 목록이 아닌 개별 게시글 URL을 등록해주세요.");
                }
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("올바른 URL 형식이 아닙니다.");
        }
    }

    private String stripQueryString(String url) {
        try {
            URI uri = URI.create(url);
            // scheme://host:port/path (fragment도 제거)
            URI clean = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, null);
            return clean.toString();
        } catch (Exception e) {
            return url;
        }
    }

    @Transactional
    public Report createReport(String nickname, String sourceUrl) {
        MapleCharacter character = mapleCharacterRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터입니다"));

        // 게시판 목록이 아닌 개별 게시글 URL인지 검증
        validatePostUrl(sourceUrl);

        // 쿼리 스트링 제거한 URL로 중복 검사 수행
        String cleanUrl = stripQueryString(sourceUrl);

        if (reportRepository.existsBySourceUrlAndMapleCharacter(cleanUrl, character)) {
            throw new IllegalStateException("이미 등록된 URL입니다.");
        }

        // 스크래핑은 원본 URL로 수행
        WebScraper.ScrapedData scrapedData = webScraper.scrape(sourceUrl);

        String combined = scrapedData.title() + " " + scrapedData.content();
        if (!combined.contains(nickname)) {
            throw new IllegalArgumentException("게시글에 해당 캐릭터 닉네임이 포함되어 있지 않습니다. (캐릭터와 연관이 있는 게시물을 등록해 주세요)");
        }

        Report savedReport = reportRepository.save(
                Report.builder()
                        .mapleCharacter(character)
                        .sourceUrl(cleanUrl)
                        .title(scrapedData.title())
                        .content(scrapedData.content())
                        .build()
        );

        // 트랜잭션 커밋 후 AI 요약 비동기 생성
        Long characterId = character.getId();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                aiSummaryClient.generateSummaryAsync(characterId);
            }
        });

        return savedReport;
    }
}
