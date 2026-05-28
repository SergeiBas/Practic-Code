package org.example.practiccode.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Entity
@Table(name = "hr_requests")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Захист від склеювання об'єктів
public class HRRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "request_type", nullable = false)
    private String requestType;

    @Column(nullable = false)
    private String status; // "На розгляді", "Погоджено", "Відхилено"

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnore // Щоб не було нескінченного циклу в JSON
    private Employee employee;

    @Column(name = "comment", length = 500)
    private String comment; // Для тексту "Потребує ІТ: так. Що зробити: ..."

    @Column(name = "description", length = 1000)
    private String description; // Загальний коментар до заявки (причина відпустки, деталі переведення тощо)
}