package dev.shin.maecheat.infrastructure.ai;

import dev.shin.maecheat.domain.character.model.MapleCharacter;
import dev.shin.maecheat.domain.character.repository.MapleCharacterRepository;
import dev.shin.maecheat.domain.report.model.Report;
import dev.shin.maecheat.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiSummaryClient {

    private final ChatClient.Builder chatClientBuilder;
    private final ReportRepository reportRepository;
    private final MapleCharacterRepository mapleCharacterRepository;

    private static final int TRIM_LENGTH = 200;

    @Async
    @Transactional
    public void generateSummaryAsync(Long characterId) {
        try {
            MapleCharacter character = mapleCharacterRepository.findById(characterId).orElseThrow();
            List<Report> reports = reportRepository.findByMapleCharacterIdOrderByUpvotesDesc(characterId);
            String summary = summarize(reports);
            character.updateAiSummary(summary);
        } catch (Exception e) {
            log.warn("AI 요약 생성 실패 (characterId={}): {}", characterId, e.getMessage());
        }
    }

    public String summarize(List<Report> reports) {
        ChatClient chatClient = chatClientBuilder.build();

        String reportTexts = reports.stream()
                .map(r -> {
                    String content = r.getContent();
//                    if (content != null && content.length() > TRIM_LENGTH * 2) {
//                        content = content.substring(0, TRIM_LENGTH)
//                                + "\n...(중략)...\n"
//                                + content.substring(content.length() - TRIM_LENGTH);
//                    }
                    return r.getTitle() + "\n" + content;
                })
                .collect(Collectors.joining("\n---\n"));

        String prompt = """
                다음은 메이플스토리 유저에 대한 평가 게시글들입니다.
                대체로 공익 목적을 위한 박제 / 신고 게시글이지만, 간혹 그렇지 않은 글도 섞여 있을 수 있습니다.
                유저에 대해 사기꾼 / 빌런 / 악질 유저 등 법률적 분쟁이 생길 수도 있는 단정적인 표현을 사용하면 절대 안 됩니다.(명예훼손 고려)
                그러나, 게시글에서 드러나는 명확한 행위 ex) 사기, 욕설, 비매너 등은 사실에 기반하여 최대한 구체적으로 표현해주세요.
                최대한 할루시네이션이 발생하지 않도록 제공되는 내용들만을 종합하여 해당 캐릭터의 평판을 한국어 3줄 이내로 간결하게 요약해주세요.
                지시 사항과 관련된 내용은 요약에 작성하지 않도록 하세요.
                시작 문장에 메이플스토리 캐릭터 혹은 유저 라는 말은 생략해도 됩니다.

                게시글 목록:
                %s
                """.formatted(reportTexts);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();  // String 반환
    }
}