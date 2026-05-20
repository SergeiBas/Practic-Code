package org.example.practiccode.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(unique = true)
    private String email;

    private String phone;

    // Зв'язок: Багато працівників можуть мати однакову посаду
    @ManyToOne
    @JoinColumn(name = "position_id") // Назва колонки зв'язку в таблиці БД
    private Position position;

    // Зв'язок: Багато працівників можуть працювати в одному підрозділі
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "hire_date")
    private LocalDate hireDate; // LocalDate ідеально підходить для типу DATE в БД
}