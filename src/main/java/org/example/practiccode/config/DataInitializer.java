package org.example.practiccode.config;

import org.example.practiccode.model.Department;
import org.example.practiccode.model.Employee;
import org.example.practiccode.model.Position;
import org.example.practiccode.model.Equipment;
import org.example.practiccode.model.HRRequest;
import org.example.practiccode.repository.DepartmentRepository;
import org.example.practiccode.repository.EmployeeRepository;
import org.example.practiccode.repository.PositionRepository;
import org.example.practiccode.repository.EquipmentRepository;
import org.example.practiccode.repository.HRRequestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final EquipmentRepository equipmentRepository; // Додали
    private final HRRequestRepository hrRequestRepository; // Додали

    // Впроваджуємо ВСІ репозиторії через конструктор
    public DataInitializer(PositionRepository positionRepository,
                           DepartmentRepository departmentRepository,
                           EmployeeRepository employeeRepository,
                           EquipmentRepository equipmentRepository,
                           HRRequestRepository hrRequestRepository) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.equipmentRepository = equipmentRepository;
        this.hrRequestRepository = hrRequestRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Перевіряємо, чи база порожня
        if (positionRepository.count() == 0 && departmentRepository.count() == 0) {

            // 1. Створюємо та зберігаємо посади
            Position dev = positionRepository.save(new Position(null, "Java Developer"));
            Position hr = positionRepository.save(new Position(null, "HR Manager"));
            Position qa = positionRepository.save(new Position(null, "QA Engineer"));

            // 2. Створюємо та зберігаємо підрозділи
            Department itDept = departmentRepository.save(new Department(null, "IT Department"));
            Department hrDept = departmentRepository.save(new Department(null, "Human Resources"));

            // 3. Створюємо перших тестових працівників
            Employee emp1 = new Employee(
                    null, "Петренко", "Іван", "Миколайович",
                    "ivan.petrenko@company.com", "+380501234567",
                    dev, itDept, LocalDate.of(2025, 1, 15), null, null
            );

            Employee emp2 = new Employee(
                    null, "Коваленко", "Олена", "Сергіївна",
                    "olena.kovalenko@company.com", "+380679876543",
                    hr, hrDept, LocalDate.of(2024, 5, 20), null, null
            );

            // Спочатку зберігаємо працівників, щоб отримати їхні ID з бази даних
            emp1 = employeeRepository.save(emp1);
            emp2 = employeeRepository.save(emp2);

            // 4. Генеруємо техніку (Equipment) для працівників
            Equipment eq1 = new Equipment();
            eq1.setItemName("Ноутбук MacBook Pro 16");
            eq1.setSerialNumber("APPLE-MX12345");
            eq1.setComment("Видано в користування. Стан: новий.");
            eq1.setEmployee(emp1); // Прив'язуємо до Івана

            Equipment eq2 = new Equipment();
            eq2.setItemName("Монітор Dell 27\"");
            eq2.setSerialNumber("DELL-98765");
            eq2.setComment("Видано для віддаленої роботи.");
            eq2.setEmployee(emp1); // Теж Івану

            Equipment eq3 = new Equipment();
            eq3.setItemName("Ноутбук ThinkPad T14");
            eq3.setSerialNumber("LENOVO-TR883");
            eq3.setComment("Видано при прийомі. Повернено мишку.");
            eq3.setEmployee(emp2); // Прив'язуємо до Олени

            equipmentRepository.save(eq1);
            equipmentRepository.save(eq2);
            equipmentRepository.save(eq3);

            // 5. Генеруємо кадрові заявки/статуси (HRRequest)
            HRRequest req1 = new HRRequest();
            req1.setRequestType("Прийом на роботу");
            req1.setStatus("Погоджено");
            req1.setCreatedDate(LocalDate.of(2025, 1, 10));
            req1.setEmployee(emp1);

            HRRequest req2 = new HRRequest();
            req2.setRequestType("Запит на нову техніку");
            req2.setStatus("На розгляді");
            req2.setCreatedDate(LocalDate.now());
            req2.setEmployee(emp1);

            HRRequest req3 = new HRRequest();
            req3.setRequestType("Прийом на роботу");
            req3.setStatus("Погоджено");
            req3.setCreatedDate(LocalDate.of(2024, 5, 15));
            req3.setEmployee(emp2);

            hrRequestRepository.save(req1);
            hrRequestRepository.save(req2);
            hrRequestRepository.save(req3);

            System.out.println(">>> База даних успішно наповнена працівниками, технікою та заявками!");
        }
    }
}