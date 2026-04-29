package dev.shin.maecheat.domain.character.repository;

import dev.shin.maecheat.domain.character.model.MapleCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MapleCharacterRepository extends JpaRepository<MapleCharacter, Long> {
    Optional<MapleCharacter> findByNickname(String nickname);
    Optional<MapleCharacter> findByOcid(String ocid);
}
