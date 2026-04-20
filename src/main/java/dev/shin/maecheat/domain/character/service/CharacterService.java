package dev.shin.maecheat.domain.character.service;

import dev.shin.maecheat.domain.character.model.Character;
import dev.shin.maecheat.domain.character.repository.CharacterRepository;
import dev.shin.maecheat.infrastructure.nexon.client.NexonApiClient;
import dev.shin.maecheat.infrastructure.nexon.dto.NexonCharacterBasicResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final NexonApiClient nexonApiClient;

    public NexonCharacterBasicResponseDto getCharacterBasic(String nickname) {
        Character character = characterRepository.findByNickname(nickname)
                .orElseGet(() -> {
                    String ocid = nexonApiClient.getCharacterId(nickname).ocid();
                    return characterRepository.save(
                            Character.builder()
                                    .ocid(ocid)
                                    .nickname(nickname)
                                    .build()
                    );
                });
        return nexonApiClient.getCharacterBasic(character.getOcid());
    }
}
