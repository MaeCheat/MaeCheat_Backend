package dev.shin.maecheat.domain.character.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "maple_character",
        uniqueConstraints = {
                // 제약조건 이름을 명확하게 지정하여 에러 로그에서 쉽게 식별 가능하도록 함
                @UniqueConstraint(name = "UK_CHARACTERS_OCID", columnNames = {"ocid"}),
                @UniqueConstraint(name = "UK_CHARACTERS_NICKNAME", columnNames = {"nickname"})
        }
)
public class MapleCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 넥슨 open api에서 제공되는 캐릭터 고유 id (ocid)
    @Column(nullable = false)
    private String ocid;

    @Column(nullable = false)
    private String nickname;

    @Column(columnDefinition = "TEXT")
    private String aiSummary;

    // 본인 요청 숨김 만료 시각 (null이면 숨김 아님)
    private LocalDateTime hiddenUntil;

    // 현재 숨김 상태인지 확인
    public boolean isOwnerHidden() {
        return hiddenUntil != null && LocalDateTime.now().isBefore(hiddenUntil);
    }

    // 숨김 요청 (days일 동안)
    public void requestHide(int days) {
        this.hiddenUntil = LocalDateTime.now().plusDays(days);
    }

    // 닉네임 변경
    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    // AI 요약 갱신
    public void updateAiSummary(String aiSummary) {
        this.aiSummary = aiSummary;
    }
}
