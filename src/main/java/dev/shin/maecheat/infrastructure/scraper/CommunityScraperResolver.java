package dev.shin.maecheat.infrastructure.scraper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommunityScraperResolver {

    private final List<CommunityScraper> scrapers;

    public CommunityScraper resolve(String url) {
        return scrapers.stream()
                .filter(s -> s.supports(url))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("현재 지원하지 않는 사이트입니다."));
    }
}
