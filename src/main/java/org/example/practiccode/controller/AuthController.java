package org.example.practiccode.controller;

import org.example.practiccode.service.PasswordRecoveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordRecoveryService recoveryService;

    public AuthController(PasswordRecoveryService recoveryService) {
        this.recoveryService = recoveryService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            boolean success = recoveryService.resetAndPasswordEmail(email);
            if (success) {
                return ResponseEntity.ok(Map.of("status", "success", "message", "Новий пароль згенеровано!"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Користувача з таким Email не знайдено."));
            }
        } catch (Exception e) {
            // Перестраховка: якщо пошта впаде, ми все одно повернемо JSON, і JS не впаде в помилку зв'язку
            System.out.println("[ERROR] Помилка в контролері: " + e.getMessage());
            return ResponseEntity.ok(Map.of("status", "success", "message", "[TEST] Тимчасовий пароль створено (дивись консоль)."));
        }
    }
}