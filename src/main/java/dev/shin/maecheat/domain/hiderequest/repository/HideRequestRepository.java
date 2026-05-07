package dev.shin.maecheat.domain.hiderequest.repository;

import dev.shin.maecheat.domain.hiderequest.model.HideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HideRequestRepository extends JpaRepository<HideRequest, Long> {
    List<HideRequest> findByProcessedFalseOrderByRequestedAtDesc();
    boolean existsByNicknameAndProcessedFalse(String nickname);
}
