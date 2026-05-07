package dev.shin.maecheat.global.admin;

import dev.shin.maecheat.domain.character.model.MapleCharacter;
import dev.shin.maecheat.domain.character.repository.MapleCharacterRepository;
import dev.shin.maecheat.domain.hiderequest.model.HideRequest;
import dev.shin.maecheat.domain.hiderequest.repository.HideRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final HideRequestRepository hideRequestRepository;
    private final MapleCharacterRepository mapleCharacterRepository;

    @Value("${app.admin.key}")
    private String adminKey;

    @GetMapping("/hide-requests")
    public ResponseEntity<?> getHideRequests(@RequestParam("key") String key) {
        if (!adminKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("인증 실패");
        }
        List<HideRequest> requests = hideRequestRepository.findByProcessedFalseOrderByRequestedAtDesc();
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/hide-requests/{id}/approve")
    @Transactional
    public ResponseEntity<?> approveHideRequest(
            @PathVariable Long id,
            @RequestParam("key") String key
    ) {
        if (!adminKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("인증 실패");
        }

        HideRequest request = hideRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없습니다."));

        MapleCharacter character = mapleCharacterRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다: " + request.getNickname()));

        character.requestHide(14);
        request.markProcessed();

        return ResponseEntity.ok(Map.of("message", request.getNickname() + " 숨김 처리 완료 (2주)"));
    }

    @PostMapping("/hide-requests/{id}/reject")
    @Transactional
    public ResponseEntity<?> rejectHideRequest(
            @PathVariable Long id,
            @RequestParam("key") String key
    ) {
        if (!adminKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("인증 실패");
        }

        HideRequest request = hideRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없습니다."));

        request.markProcessed();

        return ResponseEntity.ok(Map.of("message", request.getNickname() + " 숨김 요청 거절"));
    }
}
