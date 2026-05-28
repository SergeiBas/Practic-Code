package org.example.practiccode.controller;

import org.example.practiccode.model.Employee;
import org.example.practiccode.model.Equipment;
import org.example.practiccode.model.HRRequest; // Додав імпорт моделі заявок
import org.example.practiccode.repository.HRRequestRepository;
import org.example.practiccode.repository.EquipmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private final EquipmentRepository equipmentRepository;
    private final HRRequestRepository hrRequestRepository;

    public EmployeeRestController(EquipmentRepository equipmentRepository, HRRequestRepository hrRequestRepository) {
        this.equipmentRepository = equipmentRepository;
        this.hrRequestRepository = hrRequestRepository;
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

    // 3. Метод для зміни статусу по кнопці (Бонус)
    @PostMapping("/requests/{requestId}/status") // Зверни увагу на шлях!
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long requestId, @RequestParam String status) {

        HRRequest request = hrRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Заявку з ID " + requestId + " не знайдено"));

        String finalStatus = status;

        // Наш логічний фікс
        if ("ПОГОДЖЕНО_ОЧІКУЄ_ІТ".equals(status)) {
            String comment = request.getComment() != null ? request.getComment() : "";
            if (!comment.contains("Потребує послуг IT: Так")) {
                finalStatus = "ЗАВЕРШЕНО";
            }
        }

        request.setStatus(finalStatus);
        hrRequestRepository.save(request);

        return ResponseEntity.ok().body("{\"status\":\"success\"}");
    }
}