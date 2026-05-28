package org.example.practiccode.controller;

import org.example.practiccode.model.Role;
import org.example.practiccode.model.User;
import org.example.practiccode.model.UserInvite;
import org.example.practiccode.repository.UserInviteRepository;
import org.example.practiccode.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserInviteRepository inviteRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserInviteRepository inviteRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.inviteRepository = inviteRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. Відкриваємо сторінку введення email
    @GetMapping("/check-email")
    public String showCheckEmailPage() {
        return "check-email";
    }

    // 2. Обробляємо введену пошту
    @PostMapping("/check-email")
    public String checkEmail(@RequestParam String email, Model model) {
        String cleanEmail = email.trim().toLowerCase();

        // Перевіряємо, чи такий акаунт вже зареєстрований
        if (userRepository.existsByEmail(cleanEmail)) {
            model.addAttribute("error", "Користувач з такою поштою вже зареєстрований в системі!");
            return "check-email";
        }

        // Шукаємо пошту у вайтлісті (user_invites)
        Optional<UserInvite> inviteOpt = inviteRepository.findByEmail(cleanEmail);

        if (inviteOpt.isEmpty()) {
            model.addAttribute("error", "Вашої пошти немає у списку запрошень. Зверніться до адміністратора.");
            return "check-email";
        }

        // Якщо все ок, перекидаємо на форму реєстрації і передаємо туди email та його роль
        UserInvite invite = inviteOpt.get();
        model.addAttribute("email", cleanEmail);
        model.addAttribute("roleName", invite.getRole().getName());
        return "register";
    }

    // 3. Зберігаємо фінальні дані користувача разом із фотографією
    @PostMapping("/complete")
    public String completeRegistration(@RequestParam String email,
                                       @RequestParam String roleName,
                                       @RequestParam String firstName,
                                       @RequestParam String lastName,
                                       @RequestParam String password,
                                       @RequestParam("avatarFile") MultipartFile avatarFile) throws IOException {

        // Ще раз переконуємося по базі, що інвайт існує
        Optional<UserInvite> inviteOpt = inviteRepository.findByEmail(email);
        if (inviteOpt.isEmpty()) {
            return "redirect:/register/check-email";
        }

        UserInvite invite = inviteOpt.get();
        Role assignedRole = invite.getRole();

        // Створюємо нового користувача
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password)); // Хешуємо пароль обов'язково!
        user.setRoles(Collections.singleton(assignedRole));  // Записуємо роль з інвайту

        // --- ЛОГІКА ЗБЕРЕЖЕННЯ АВАТАРКИ НА ДИСК ---
        if (avatarFile != null && !avatarFile.isEmpty()) {
            // Шлях, куди фізично збережеться картинка всередині проекту
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/avatars/";

            File folder = new File(uploadDir);
            if (!folder.exists()) {
                folder.mkdirs(); // Створюємо папки, якщо їх ще немає
            }

            // Генеруємо унікальне ім'я файлу, щоб уникнути однакових назв
            String uniqueFileName = UUID.randomUUID().toString() + "_" + avatarFile.getOriginalFilename();
            File destinationFile = new File(uploadDir + uniqueFileName);

            avatarFile.transferTo(destinationFile); // Фізично переносимо файл

            // Зберігаємо відносний шлях, який буде використовувати Thymeleaf в тегу <img src="...">
            user.setAvatarPath("/images/avatars/" + uniqueFileName);
        } else {
            // Якщо фото не завантажили — ставимо дефолтну Unsplash заглушку, яка у тебе вже була
            user.setAvatarPath("https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=200");
        }

        // Зберігаємо користувача в БД
        userRepository.save(user);

        // Видаляємо інвайт із вайтлісту, щоб ним більше не скористалися
        inviteRepository.delete(invite);

        // Після успіху відправляємо на логін з гарним параметром
        return "redirect:/login?registered=true";
    }
}