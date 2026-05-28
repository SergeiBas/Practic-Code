package org.example.practiccode.repository;

import org.example.practiccode.model.UserInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserInviteRepository extends JpaRepository<UserInvite, Long> {
    Optional<UserInvite> findByEmail(String email);
    boolean existsByEmail(String email);
}