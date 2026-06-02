package org.example.practiccode.service;

import org.example.practiccode.model.User; // вкажи свою модель користувача
import org.example.practiccode.repository.UserRepository; // вкажи свій репозиторій
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordRecoveryService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender; // Повертаємо реальну пошту

    public PasswordRecoveryService(UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public boolean resetAndPasswordEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);

        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);

        // Відправка реального листа через сервери Google
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("sergei.bas06@gmail.com"); // Твоя пошта-відправник
            message.setTo(email); // Пошта користувача, який забув пароль
            message.setSubject("Тимчасовий пароль для входу в систему");
            message.setText("Вітаємо!\n\nВаш новий тимчасовий пароль: " + rawPassword + "\n\nБудь ласка, змініть його після входу.");

            mailSender.send(message);
            System.out.println("[SMTP] Лист успішно надіслано на " + email);
        } catch (Exception e) {
            System.out.println("[SMTP ERROR] Не вдалося надіслати лист: " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }
}
