package dev.shin.maecheat.domain.character.controller;

import dev.shin.maecheat.domain.character.dto.CharacterResponse;
import dev.shin.maecheat.domain.character.service.MapleCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
