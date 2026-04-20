package dev.shin.maecheat.domain.character.controller;

import dev.shin.maecheat.domain.character.service.MapleCharacterService;
import dev.shin.maecheat.infrastructure.nexon.dto.NexonCharacterBasicResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MapleCharacterController {
    private final MapleCharacterService mapleCharacterService;

    @GetMapping("/basic")
    public NexonCharacterBasicResponseDto getCharacterBasic(
            @RequestParam("nickname") String nickname
    ) {
        return mapleCharacterService.getCharacterBasic(nickname);
    }
}
