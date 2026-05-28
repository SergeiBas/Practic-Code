package org.example.practiccode.repository;

import org.example.practiccode.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    java.util.Optional<Position> findByTitleIgnoreCase(String title);
}