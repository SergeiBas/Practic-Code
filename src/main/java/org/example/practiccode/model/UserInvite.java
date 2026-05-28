package org.example.practiccode.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_invites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}