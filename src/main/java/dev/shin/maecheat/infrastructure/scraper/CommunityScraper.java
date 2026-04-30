package dev.shin.maecheat.infrastructure.scraper;

public interface CommunityScraper {

    record ScrapedData(String title, String content, int upvotes) {}

    /** 이 스크래퍼가 해당 URL을 처리할 수 있는지 */
    boolean supports(String url);

    /** URL 형식 검증 (게시글 URL인지 등) */
    void validateUrl(String url);

    /** 게시글 스크래핑 */
    ScrapedData scrape(String url);

    /** 등록에 필요한 최소 추천 수 (-1이면 검증 안 함) */
    int getMinUpvotes();
}
