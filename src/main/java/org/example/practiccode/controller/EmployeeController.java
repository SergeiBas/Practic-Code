package org.example.practiccode.controller;

import org.example.practiccode.model.Employee;
import org.example.practiccode.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    // Впроваджуємо сервіс через конструктор
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Обробляємо запит на головну сторінку http://localhost:8080/
    @GetMapping("/")
    public String viewHomePage(Model model) {
        // Отримуємо список працівників із сервісу
        List<Employee> listEmployees = employeeService.getAllEmployees();

        // Передаємо цей список у HTML-шаблон Thymeleaf під ім'ям "employees"
        model.addAttribute("employees", listEmployees);

        // Повертаємо назву HTML-файлу (index.html), який лежить у templates
        return "index";
    }
}