package dev.shin.maecheat.domain.character.controller;

import dev.shin.maecheat.domain.character.dto.CharacterResponse;
import dev.shin.maecheat.domain.character.service.MapleCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/maple-characters")
@RequiredArgsConstructor
public class MapleCharacterController {
    private final MapleCharacterService mapleCharacterService;

    @GetMapping("/basic")
    public CharacterResponse getCharacterBasic(
            @RequestParam("nickname") String nickname
    ) {
        return mapleCharacterService.getCharacterBasic(nickname);
    }

    @PostMapping("/{nickname}/hide")
    public ResponseEntity<Map<String, String>> requestHide(
            @PathVariable String nickname
    ) {
        mapleCharacterService.requestHide(nickname);
        return ResponseEntity.ok(Map.of("message", "숨김 처리가 완료되었습니다. 2주간 유지됩니다."));
    }
}
