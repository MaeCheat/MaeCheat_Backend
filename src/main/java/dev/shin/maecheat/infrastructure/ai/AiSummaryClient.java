package dev.shin.maecheat.infrastructure.ai;

import dev.shin.maecheat.domain.report.model.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiSummaryClient {

    private final ChatClient.Builder chatClientBuilder;

    public String summarize(List<Report> reports) {
        ChatClient chatClient = chatClientBuilder.build();

        String reportTexts = reports.stream()
                .map(r -> r.getTitle() + "\n" + r.getContent())
                .collect(Collectors.joining("\n---\n"));

        String prompt = """
                다음은 메이플스토리 캐릭터에 대한 신고 게시글 목록입니다.
                이 내용을 종합하여 해당 캐릭터의 평판을 3줄로 요약해주세요.
                
                게시글 목록:
                %s
                """.formatted(reportTexts);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();  // String 반환
    }
}