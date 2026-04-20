package dev.shin.maecheat.domain.character.service;

import dev.shin.maecheat.domain.character.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
}
