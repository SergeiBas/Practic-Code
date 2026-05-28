package org.example.practiccode.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "equipment")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Важливо! Порівнюємо тільки по ID
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    @EqualsAndHashCode.Include // Тільки це поле бере участь у порівнянні
    private Long id;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "serial_number")
    private String serialNumber;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnore // КРИТИЧНО ВАЖЛИВО! Запобігає нескінченному колу при конвертації в JSON
    private Employee employee;
}