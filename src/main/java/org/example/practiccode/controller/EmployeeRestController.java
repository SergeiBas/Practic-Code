package org.example.practiccode.controller;

import org.example.practiccode.model.*;
import org.example.practiccode.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private final EquipmentRepository equipmentRepository;
    private final HRRequestRepository hrRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public EmployeeRestController(EquipmentRepository equipmentRepository, HRRequestRepository hrRequestRepository, EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, PositionRepository positionRepository) {
        this.equipmentRepository = equipmentRepository;
        this.hrRequestRepository = hrRequestRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    // 1. Метод для отримання техніки (Вже працював)
    @GetMapping("/{id}/equipment")
    public List<Equipment> getEmployeeEquipment(@PathVariable Long id) {
        return equipmentRepository.findByEmployeeId(id);
    }

    // 2. МЕТОД, ЯКОГО НЕ ВИСТАЧАЛО (Отримання заявок)
    @GetMapping("/{id}/requests")
    public List<HRRequest> getEmployeeRequests(@PathVariable Long id) {
        return hrRequestRepository.findByEmployeeId(id);
    }

    // 3. МЕТОД, ЯКИЙ ЗМІНЮЄ СТАТУС
    @PostMapping("/requests/{requestId}/status")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long requestId, @RequestParam String status) {

        HRRequest request = hrRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Заявку з ID " + requestId + " не знайдено"));

        // 1. ЯКЩО КЕРІВНИК ВІДХИЛЯЄ ПРИЙОМ НА РОБОТУ (Видаляємо повністю)
        if ("ВІДХИЛЕНО".equals(status)) {
            String type = request.getRequestType() != null ? request.getRequestType().toUpperCase() : "";

            if (type.contains("ПРИЙОМ")) {
                Employee employee = request.getEmployee();

                hrRequestRepository.delete(request);

                if (employee != null) {
                    equipmentRepository.findByEmployeeId(employee.getId())
                            .forEach(equipmentRepository::delete);
                    employeeRepository.delete(employee);
                }
                return ResponseEntity.ok().body("{\"status\":\"deleted\"}");
            }
        }

        // 2. ОБЧИСЛЮЄМО ФІНАЛЬНИЙ СТАТУС (Важливо зробити це НАД кадровими змінами)
        String finalStatus = status;

        if ("ПОГОДЖЕНО_ОЧІКУЄ_ІТ".equals(status)) {
            String comment = request.getComment() != null ? request.getComment() : "";
            if (!comment.contains("Потребує послуг IT: Так")) {
                finalStatus = "ЗАВЕРШЕНО";
            }
        }

        // 3. ЯКЩО ЗАЯВКА ОФІЦІЙНО ЗАВЕРШЕНА — ОНОВЛЮЄМО ДАНІ ПРАЦІВНИКА
        if ("ЗАВЕРШЕНО".equals(finalStatus)) {
            Employee employee = request.getEmployee();
            if (employee != null) {
                String type = request.getRequestType();

                // Сценарій А: Звільнення (перевід в 99-й відділ)
                if ("Звільнення".equalsIgnoreCase(type)) {
                    Department archiveDept = departmentRepository.findById(99L).orElse(null);
                    if (archiveDept != null) {
                        employee.setDepartment(archiveDept);
                        employeeRepository.save(employee);
                    }
                }

                // Сценарій Б: Зміна посади / відділу (Кадрове переведення)
                else if ("Зміна посади / відділу".equalsIgnoreCase(type)) {
                    String description = request.getDescription();
                    if (description != null) {
                        try {
                            // Оскільки нова посада тепер буде в самому кінці рядка, цей спліт спрацює ідеально!
                            String deptName = description.split("Новий відділ: '")[1].split("'")[0].trim();
                            String posTitle = description.split("Нова посада: '")[1].split("'")[0].trim();

                            Department department = departmentRepository.findByNameIgnoreCase(deptName)
                                    .orElseGet(() -> {
                                        Department d = new Department();
                                        d.setName(deptName);
                                        return departmentRepository.save(d);
                                    });

                            Position position = positionRepository.findByTitleIgnoreCase(posTitle)
                                    .orElseGet(() -> {
                                        Position p = new Position();
                                        p.setTitle(posTitle);
                                        return positionRepository.save(p);
                                    });

                            employee.setDepartment(department);
                            employee.setPosition(position);
                            employeeRepository.save(employee);

                        } catch (Exception e) {
                            System.err.println("Помилка автоматичного оновлення даних: " + e.getMessage());
                        }
                    }
                }
            }
        }

        // 4. ЗБЕРІГАЄМО САМУ ЗАЯВКУ З ФІНАЛЬНИМ СТАТУСОМ
        request.setStatus(finalStatus);
        hrRequestRepository.save(request);

        return ResponseEntity.ok().body("{\"status\":\"success\"}");
    }

    // 4. МЕТОД ДЛЯ ВНЕСЕННЯ ПРАВОК КЕРІВНИКОМ ТА АВТОМАТИЧНОГО ПОГОДЖЕННЯ
    @PostMapping("/requests/{requestId}/amend")
    public ResponseEntity<?> amendAndApproveRequest(@PathVariable Long requestId,
                                                    @RequestParam String newDescription) {

        HRRequest request = hrRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Заявку з ID " + requestId + " не знайдено"));

        // Формуємо фінальний текст: додаємо мітку "Керівник: ", якщо її там ще немає
        String cleanText = newDescription.trim();
        String updatedDescription;
        if (cleanText.startsWith("Керівник:")) {
            updatedDescription = cleanText;
        } else {
            updatedDescription = "Керівник: " + cleanText;
        }

        request.setDescription(updatedDescription);

        // Автоматично погоджуємо та відправляємо далі на IT
        request.setStatus("ПОГОДЖЕНО_ОЧІКУЄ_ІТ");

        hrRequestRepository.save(request);

        return ResponseEntity.ok().body("{\"status\":\"success\"}");
    }
}