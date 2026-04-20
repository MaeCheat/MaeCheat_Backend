package dev.shin.maecheat.infrastructure.nexon.client;

import dev.shin.maecheat.infrastructure.nexon.dto.NexonOcidResponseDto;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/maplestory/v1")
public interface NexonApiClient {

    @GetExchange("/id")
    NexonOcidResponseDto getCharacterId(
            @RequestParam("character_name") String characterName
    );
}
