package org.example.practiccode.controller;

import org.springframework.security.core.Authentication;
import org.example.practiccode.model.User;
import org.example.practiccode.model.UserInvite;
import org.example.practiccode.repository.RoleRepository;
import org.example.practiccode.repository.UserInviteRepository;
import org.example.practiccode.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final UserInviteRepository inviteRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public ProfileController(UserRepository userRepository, UserInviteRepository inviteRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.inviteRepository = inviteRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "profile"; // Повертає файл profile.html
    }
    
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user,
                                @RequestParam(required = false) String password,
                                @RequestParam("avatarFile") MultipartFile avatarFile,
                                Authentication auth) throws IOException {

        // Знаходимо користувача за email (це безпечніше, ніж через ID з форми)
        User existingUser = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));

        // 1. Оновлення даних
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        if(password != null && !password.isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(password));
        }

        // 2. Логіка заміни фото
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/avatars/";

            // А) Видаляємо старий файл, якщо він існує і не є посиланням на Unsplash
            if (existingUser.getAvatarPath() != null && !existingUser.getAvatarPath().startsWith("http")) {
                File oldFile = new File(System.getProperty("user.dir") + "/src/main/resources/static" + existingUser.getAvatarPath());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            // Б) Зберігаємо новий файл
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();

            String uniqueFileName = UUID.randomUUID().toString() + "_" + avatarFile.getOriginalFilename();
            avatarFile.transferTo(new File(uploadDir + uniqueFileName));

            // В) Оновлюємо шлях у об'єкті
            existingUser.setAvatarPath("/images/avatars/" + uniqueFileName);
        }

        // 3. Зберігаємо оновлену сутність
        userRepository.save(existingUser);

        return "redirect:/profile?success";
    }
    @PostMapping("/admin/add-invite")
    public String addInvite(@RequestParam String email, @RequestParam Long roleId) {
        UserInvite invite = new UserInvite();
        invite.setEmail(email);
        invite.setRole(roleRepository.findById(roleId).get());
        inviteRepository.save(invite);
        return "redirect:/profile";
    }
}