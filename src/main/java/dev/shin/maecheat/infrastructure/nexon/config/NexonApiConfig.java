package dev.shin.maecheat.infrastructure.nexon.config;

import dev.shin.maecheat.infrastructure.nexon.client.NexonApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(types = NexonApiClient.class)
public class NexonApiConfig {

    @Value("${nexon.api-key}")
    private String apiKey;

    @Bean
    RestClientHttpServiceGroupConfigurer nexonConfigurer() {
        return groups -> groups.forEachClient((group, builder) ->
                builder.baseUrl("https://open.api.nexon.com")
                        .defaultHeader("x-nxopen-api-key", apiKey)
        );
    }
}