package org.example.practiccode.service;

import org.springframework.transaction.annotation.Transactional;
import org.example.practiccode.model.Employee;
import org.example.practiccode.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Впроваджуємо репозиторій через конструктор
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Метод, який повертає список усіх працівників із бази даних
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        // Примусово ініціалізуємо списки, поки сесія відкрита
        for (Employee emp : employees) {
            emp.getEquipments().size();
            emp.getHrRequests().size();
        }
        return employees;
    }

    public void saveEmployee(Employee employee) {
        this.employeeRepository.save(employee);
    }
}