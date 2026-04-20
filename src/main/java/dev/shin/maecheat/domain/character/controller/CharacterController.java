package dev.shin.maecheat.domain.character.controller;

import dev.shin.maecheat.domain.character.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class CharacterController {
    private final CharacterService characterService;
}
