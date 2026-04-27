package dev.shin.maecheat.infrastructure.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WebScraper {

    public record ScrapedData(String title, String content) {}

    public ScrapedData scrape(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            String title = doc.title();
            String content = doc.body().text();

            return new ScrapedData(title, content);
        } catch (IOException e) {
            throw new IllegalArgumentException("해당 URL에서 데이터를 가져올 수 없습니다: " + url, e);
        }
    }
}
