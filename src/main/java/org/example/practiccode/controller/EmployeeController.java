package org.example.practiccode.controller;

import org.example.practiccode.model.Employee;
import org.example.practiccode.service.EmployeeService;
// Імпортуємо репозиторії для посад та департаментів (перевір їхні точні назви у себе)
import org.example.practiccode.repository.DepartmentRepository;
import org.example.practiccode.repository.PositionRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentRepository departmentRepository; // Напряму репозиторій
    private final PositionRepository positionRepository;     // Напряму репозиторій

    // Впроваджуємо все через конструктор
    public EmployeeController(EmployeeService employeeService,
                              DepartmentRepository departmentRepository,
                              PositionRepository positionRepository) {
        this.employeeService = employeeService;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    @GetMapping({"/", "/employees"})
    public String viewHomePage(Model model) {
        // 1. Виведення списку працівників
        List<Employee> listEmployees = employeeService.getAllEmployees();
        model.addAttribute("employees", listEmployees);

        // 2. Порожній об'єкт для форми додавання
        Employee newEmployee = new Employee();
        model.addAttribute("employee", newEmployee);

        // 3. Викликаємо стандартний метод .findAll() прямо з репозиторіїв
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("positions", positionRepository.findAll());

        return "index";
    }

    @PostMapping("/employees/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/";
    }
}