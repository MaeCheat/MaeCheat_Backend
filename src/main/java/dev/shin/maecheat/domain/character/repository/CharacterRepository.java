package dev.shin.maecheat.domain.character.repository;

import dev.shin.maecheat.domain.character.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<Character> findByNickname(String nickname);
}
