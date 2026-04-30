package dev.shin.maecheat.infrastructure.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WebScraper {

    public record ScrapedData(String title, String content, int upvotes) {}

    public ScrapedData scrape(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            String title;
            String content;

            if (url.contains("inven.co.kr")) {
                title = extractText(doc, ".articleTitle", doc.title());
                content = extractText(doc, "#powerbbsContent", doc.body().text());
            } else {
                title = doc.title();
                content = doc.body().text();
            }

            int upvotes = parseInvenUpvotes(url, doc);

            return new ScrapedData(title, content, upvotes);
        } catch (IOException e) {
            throw new IllegalArgumentException("해당 URL에서 데이터를 가져올 수 없습니다. URL을 확인해주세요.");
        }
    }

    private String extractText(Document doc, String selector, String fallback) {
        Element el = doc.selectFirst(selector);
        return (el != null) ? el.text() : fallback;
    }

    private int parseInvenUpvotes(String url, Document doc) {
        if (!url.contains("inven.co.kr")) {
            return -1; // 인벤이 아닌 사이트는 추천 수 검증 대상 아님
        }

        // 인벤 추천 수 셀렉터 후보들
        String[] selectors = {
                "#bbsRecommendNum1",
                "a.bttnRecommend"
        };

        for (String selector : selectors) {
            Element el = doc.selectFirst(selector);
            if (el != null) {
                try {
                    return Integer.parseInt(el.text().replaceAll("[^0-9]", ""));
                } catch (NumberFormatException ignored) {}
            }
        }

        return -1; // 추천 수를 찾지 못한 경우
    }
}
