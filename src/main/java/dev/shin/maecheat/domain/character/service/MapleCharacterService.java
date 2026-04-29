package dev.shin.maecheat.domain.character.service;

import dev.shin.maecheat.domain.character.dto.CharacterResponse;
import dev.shin.maecheat.domain.character.model.MapleCharacter;
import dev.shin.maecheat.domain.character.repository.MapleCharacterRepository;
import dev.shin.maecheat.infrastructure.nexon.client.NexonApiClient;
import dev.shin.maecheat.infrastructure.nexon.dto.NexonCharacterBasicResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MapleCharacterService {
    private final MapleCharacterRepository mapleCharacterRepository;
    private final NexonApiClient nexonApiClient;

    @Transactional
    public CharacterResponse getCharacterBasic(String nickname) {
        String ocid = nexonApiClient.getCharacterId(nickname).ocid();

        // ocid가 같은데 닉네임이 다른 경우 닉네임 업데이트
        // ocid로 조회되지 않는 경우 새 row 저장
        MapleCharacter character = mapleCharacterRepository.findByOcid(ocid)
                .map(existing -> {
                    if (!existing.getNickname().equals(nickname)) {
                        existing.updateNickname(nickname);
                    }
                    return existing;
                })
                .orElseGet(() -> mapleCharacterRepository.save(
                        MapleCharacter.builder()
                                .ocid(ocid)
                                .nickname(nickname)
                                .build()
                ));

        NexonCharacterBasicResponseDto nexon = nexonApiClient.getCharacterBasic(ocid);
        return CharacterResponse.of(nexon, character.getAiSummary());
    }
}
