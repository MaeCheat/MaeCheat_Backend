package dev.shin.maecheat.domain.character.controller;

import dev.shin.maecheat.domain.character.service.CharacterService;
import dev.shin.maecheat.infrastructure.nexon.dto.NexonCharacterBasicResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CharacterController {
    private final CharacterService characterService;

    @GetMapping("/basic")
    public NexonCharacterBasicResponseDto getCharacterBasic(
            @RequestParam("nickname") String nickname
    ) {
        return characterService.getCharacterBasic(nickname);
    }
}