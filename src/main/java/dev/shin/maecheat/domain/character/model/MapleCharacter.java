package dev.shin.maecheat.domain.character.model;

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

    private String aiSummary;

    // 닉네임 변경
    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }
}
