package dev.shin.maecheat.infrastructure.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
public class InvenScraper implements CommunityScraper {

    private static final int MIN_UPVOTES = 30;

    @Override
    public boolean supports(String url) {
        try {
            String host = URI.create(url).getHost();
            return host != null && host.contains("inven.co.kr");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void validateUrl(String url) {
        try {
            URI uri = URI.create(url);
            String path = uri.getPath();
            String[] segments = path.split("/");

            // /board/{game}/{boardId}/{postId} 형태여야 함
            if (segments.length < 5 || !segments[segments.length - 1].matches("\\d+")) {
                throw new IllegalArgumentException("게시판 목록이 아닌 개별 게시글 URL을 등록해주세요.");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("올바른 URL 형식이 아닙니다.");
        }
    }

    private String convertToDesktopUrl(String url) {
        return url.replace("://m.inven.co.kr", "://www.inven.co.kr");
    }

    @Override
    public ScrapedData scrape(String url) {
        try {
            String desktopUrl = convertToDesktopUrl(url);
            Document doc = Jsoup.connect(desktopUrl)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            String title = extractText(doc, ".articleTitle", doc.title());
            String content = extractText(doc, "#powerbbsContent", doc.body().text());
            int upvotes = parseUpvotes(doc);

            return new ScrapedData(title, content, upvotes);
        } catch (IOException e) {
            throw new IllegalArgumentException("해당 URL에서 데이터를 가져올 수 없습니다. URL을 확인해주세요.");
        }
    }

    @Override
    public int getMinUpvotes() {
        return MIN_UPVOTES;
    }

    private String extractText(Document doc, String selector, String fallback) {
        Element el = doc.selectFirst(selector);
        return (el != null) ? el.text() : fallback;
    }

    private int parseUpvotes(Document doc) {
        String[] selectors = {"#bbsRecommendNum1", "a.bttnRecommend"};

        for (String selector : selectors) {
            Element el = doc.selectFirst(selector);
            if (el != null) {
                try {
                    return Integer.parseInt(el.text().replaceAll("[^0-9]", ""));
                } catch (NumberFormatException ignored) {}
            }
        }

        throw new IllegalArgumentException("추천 수를 확인할 수 없습니다. PC 버전 URL로 다시 시도해주세요.");
    }
}
