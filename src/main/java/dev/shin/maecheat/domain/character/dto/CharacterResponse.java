package dev.shin.maecheat.domain.character.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.shin.maecheat.infrastructure.nexon.dto.NexonCharacterBasicResponseDto;

public record CharacterResponse(
        @JsonProperty("character_name") String characterName,
        @JsonProperty("world_name") String worldName,
        @JsonProperty("character_class") String characterClass,
        @JsonProperty("character_level") int characterLevel,
        @JsonProperty("character_guild_name") String characterGuildName,
        @JsonProperty("character_image") String characterImage,
        @JsonProperty("ai_summary") String aiSummary
) {
    public static CharacterResponse of(NexonCharacterBasicResponseDto nexon, String aiSummary) {
        return new CharacterResponse(
                nexon.characterName(),
                nexon.worldName(),
                nexon.characterClass(),
                nexon.characterLevel(),
                nexon.characterGuildName(),
                nexon.characterImage(),
                aiSummary
        );
    }
}
