package dev.shin.maecheat.domain.character.controller;

import dev.shin.maecheat.domain.character.dto.CharacterResponse;
import dev.shin.maecheat.domain.character.service.MapleCharacterService;
import dev.shin.maecheat.domain.hiderequest.model.HideRequest;
import dev.shin.maecheat.domain.hiderequest.repository.HideRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/maple-characters")
@RequiredArgsConstructor
public class MapleCharacterController {
    private final MapleCharacterService mapleCharacterService;
    private final HideRequestRepository hideRequestRepository;

    @GetMapping("/basic")
    public CharacterResponse getCharacterBasic(
            @RequestParam("nickname") String nickname
    ) {
        return mapleCharacterService.getCharacterBasic(nickname);
    }

    public record HideRequestBody(String reason) {}

    @PostMapping("/{nickname}/hide")
    public ResponseEntity<Map<String, String>> requestHide(
            @PathVariable String nickname,
            @RequestBody HideRequestBody body,
            HttpServletRequest request
    ) {
        if (hideRequestRepository.existsByNicknameAndProcessedFalse(nickname)) {
            return ResponseEntity.ok(Map.of("message", "이미 숨김 요청이 접수된 캐릭터입니다."));
        }

        String ip = getClientIp(request);
        hideRequestRepository.save(HideRequest.builder()
                .nickname(nickname)
                .requesterIp(ip)
                .reason(body.reason() != null ? body.reason() : "")
                .build());

        return ResponseEntity.ok(Map.of("message", "숨김 요청이 접수되었습니다. 관리자 확인 후 처리됩니다."));
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
