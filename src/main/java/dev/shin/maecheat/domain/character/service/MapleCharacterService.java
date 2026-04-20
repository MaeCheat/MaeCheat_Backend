package dev.shin.maecheat.domain.character.service;

import dev.shin.maecheat.domain.character.model.MapleCharacter;
import dev.shin.maecheat.domain.character.repository.MapleCharacterRepository;
import dev.shin.maecheat.infrastructure.nexon.client.NexonApiClient;
import dev.shin.maecheat.infrastructure.nexon.dto.NexonCharacterBasicResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapleCharacterService {
    private final MapleCharacterRepository mapleCharacterRepository;
    private final NexonApiClient nexonApiClient;

    public NexonCharacterBasicResponseDto getCharacterBasic(String nickname) {
        // DB에 있으면 ocid 재사용, 없으면 조회 후 저장
        MapleCharacter character = mapleCharacterRepository.findByNickname(nickname)
                .orElseGet(() -> {
                    String ocid = nexonApiClient.getCharacterId(nickname).ocid();
                    return mapleCharacterRepository.save(
                            MapleCharacter.builder()
                                    .ocid(ocid)
                                    .nickname(nickname)
                                    .build()
                    );
                });
        return nexonApiClient.getCharacterBasic(character.getOcid());
    }
}
