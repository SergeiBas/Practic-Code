package org.example.practiccode.repository;

import org.example.practiccode.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    // Spring сам згенерує SQL-запит на основі назви цього методу!
    List<Equipment> findByEmployeeId(Long employeeId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Equipment e WHERE e.id = :id")
    void deleteByIdDirectly(@Param("id") Long id);
}