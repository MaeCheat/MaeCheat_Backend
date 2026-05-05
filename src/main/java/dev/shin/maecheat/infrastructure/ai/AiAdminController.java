package dev.shin.maecheat.infrastructure.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/ai")
@RequiredArgsConstructor
public class AiAdminController {

    private final AiSummaryClient aiSummaryClient;

    @PostMapping("/regenerate-all")
    public String regenerateAll() {
        aiSummaryClient.regenerateAll();
        return "요약 재생성 시작됨";
    }
}
