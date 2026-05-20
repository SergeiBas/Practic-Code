package org.example.practiccode.config;

import org.example.practiccode.model.Department;
import org.example.practiccode.model.Employee;
import org.example.practiccode.model.Position;
import org.example.practiccode.repository.DepartmentRepository;
import org.example.practiccode.repository.EmployeeRepository;
import org.example.practiccode.repository.PositionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    // Впроваджуємо наші репозиторії через конструктор
    public DataInitializer(PositionRepository positionRepository,
                           DepartmentRepository departmentRepository,
                           EmployeeRepository employeeRepository) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Перевіряємо, чи база вже не заповнена, щоб не дублювати дані при кожному перезапуску
        if (positionRepository.count() == 0 && departmentRepository.count() == 0) {

            // 1. Створюємо та зберігаємо посади
            Position dev = positionRepository.save(new Position(null, "Java Developer"));
            Position hr = positionRepository.save(new Position(null, "HR Manager"));
            Position qa = positionRepository.save(new Position(null, "QA Engineer"));

            // 2. Створюємо та зберігаємо підрозділи
            Department itDept = departmentRepository.save(new Department(null, "IT Department"));
            Department hrDept = departmentRepository.save(new Department(null, "Human Resources"));

            // 3. Створюємо перших тестових працівників із прив'язкою до посад і відділів
            Employee emp1 = new Employee(
                    null,
                    "Петренко",
                    "Іван",
                    "Миколайович",
                    "ivan.petrenko@company.com",
                    "+380501234567",
                    dev,     // передаємо об'єкт посади
                    itDept,  // передаємо об'єкт відділу
                    LocalDate.of(2025, 1, 15)
            );

            Employee emp2 = new Employee(
                    null,
                    "Коваленко",
                    "Олена",
                    "Сергіївна",
                    "olena.kovalenko@company.com",
                    "+380679876543",
                    hr,      // передаємо об'єкт посади
                    hrDept,  // передаємо об'єкт відділу
                    LocalDate.of(2024, 5, 20)
            );

            employeeRepository.save(emp1);
            employeeRepository.save(emp2);

            System.out.println(">>> База даних успішно наповнена початковими тестовими даними!");
        }
    }
}