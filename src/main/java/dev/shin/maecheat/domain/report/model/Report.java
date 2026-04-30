package dev.shin.maecheat.domain.report.model;

import dev.shin.maecheat.domain.character.model.MapleCharacter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "report",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_REPORT_URL_CHARACTER", columnNames = {"source_url", "maple_character_id"})
        }
)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sourceUrl;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    @Column(nullable = false)
    private int upvotes = 0;

    @Builder.Default
    @Column(nullable = false)
    private int downvotes = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maple_character_id", nullable = false)
    private MapleCharacter mapleCharacter;

    public void upvote() {
        this.upvotes++;
    }

    public void downvote() {
        this.downvotes++;
    }

    public int getScore() {
        return upvotes - downvotes;
    }

    public boolean isHidden() {
        return (downvotes - upvotes) > 5;
    }
}
