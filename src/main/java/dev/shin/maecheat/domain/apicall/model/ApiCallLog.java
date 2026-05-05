package dev.shin.maecheat.domain.apicall.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "api_call_log",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_API_CALL_LOG_DATE", columnNames = {"callDate"})
        }
)
public class ApiCallLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate callDate;

    @Column(nullable = false)
    private long callCount;

    public void incrementCount() {
        this.callCount++;
    }
}
