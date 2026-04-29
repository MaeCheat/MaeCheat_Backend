package dev.shin.maecheat.domain.report.model;

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
        name = "vote",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_VOTE_REPORT_IP", columnNames = {"report_id", "voter_ip"})
        }
)
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Column(name = "voter_ip", nullable = false)
    private String voterIp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteType voteType;

}
