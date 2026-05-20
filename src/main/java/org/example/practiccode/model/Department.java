package org.example.practiccode.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    @Column(nullable = false)
    private String name; // Назва підрозділу (наприклад: "IT", "HR")

    // Поле manager_id з чернетки поки що опустимо,
    // щоб не ускладнювати, додамо його, коли створимо таблицю Users
}