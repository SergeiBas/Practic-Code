package org.example.practiccode.controller;

import org.example.practiccode.model.Employee;
import org.example.practiccode.model.Department;
import org.example.practiccode.model.Position;
import org.example.practiccode.model.Equipment;
import org.example.practiccode.model.HRRequest;
import org.example.practiccode.service.EmployeeService;
import org.example.practiccode.repository.DepartmentRepository;
import org.example.practiccode.repository.PositionRepository;
import org.example.practiccode.repository.EquipmentRepository;
import org.example.practiccode.repository.HRRequestRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EquipmentRepository equipmentRepository;
    private final HRRequestRepository hrRequestRepository;

    // Впроваджуємо всі залежності через єдиний конструктор
    public EmployeeController(EmployeeService employeeService,
                              DepartmentRepository departmentRepository,
                              PositionRepository positionRepository,
                              EquipmentRepository equipmentRepository,
                              HRRequestRepository hrRequestRepository) {
        this.employeeService = employeeService;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.equipmentRepository = equipmentRepository;
        this.hrRequestRepository = hrRequestRepository;
    }

    // 1. ГОЛОВНА СТОРІНКА (Групування за відділами + Звільнені по ID 99)
    @GetMapping({"/", "/employees"})
    public String viewHomePage(Model model) {
        List<Employee> allEmployees = employeeService.getAllEmployees();

        // 1. Працівники, які потребують уваги (мають активні процеси) - залишаємо без змін
        List<Employee> activeEmployees = allEmployees.stream()
                .filter(emp -> emp.getHrRequests() != null && emp.getHrRequests().stream()
                        .anyMatch(req -> "НА_РОЗГЛЯДІ_КЕРІВНИКА".equals(req.getStatus())
                                || "ПОГОДЖЕНО_ОЧІКУЄ_ІТ".equals(req.getStatus())))
                .toList();

        // 2. ФІЛЬТР ЗВІЛЬНЕНИХ: Тепер це ті, у кого department_id == 99
        List<Employee> firedEmployees = allEmployees.stream()
                .filter(emp -> emp.getDepartment() != null && emp.getDepartment().getId() == 99)
                .sorted((e1, e2) -> {
                    String name1 = e1.getLastName() != null ? e1.getLastName() : "";
                    String name2 = e2.getLastName() != null ? e2.getLastName() : "";
                    return name1.compareToIgnoreCase(name2);
                })
                .toList();

        // 3. Діючі працівники (В штаті): НЕ потребують уваги і НЕ в 99-му відділі
        List<Employee> inStaffEmployees = allEmployees.stream()
                .filter(emp -> !activeEmployees.contains(emp) && !firedEmployees.contains(emp))
                .toList();

        // 4. ГРУПУВАННЯ ЗА ВІДДІЛАМИ для списку "В штаті"
        java.util.Map<String, List<Employee>> employeesByDepartment = inStaffEmployees.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        emp -> emp.getDepartment() != null ? emp.getDepartment().getName() : "Без відділу"
                ));

        model.addAttribute("activeEmployees", activeEmployees);
        model.addAttribute("employeesByDepartment", employeesByDepartment);
        model.addAttribute("firedEmployees", firedEmployees); // Передаємо відфільтрований за ID 99 список в архів

        return "index";
    }

    // 2. Відкриття форми (Тепер повертає новий add-request)
    @GetMapping("/employees/add-form")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("allEmployees", employeeService.getAllEmployees());
        return "add-request"; // Перейменували шаблон
    }

    // 3. Оновлений обробник із двома різними коментарями
    @PostMapping("/employees/create-request")
    public String createRequest(@RequestParam String requestType,
                                @RequestParam(required = false) Long existingEmployeeId,
                                @RequestParam(required = false) String firstName,
                                @RequestParam(required = false) String lastName,
                                @RequestParam(required = false) String middleName,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String departmentName,
                                @RequestParam(required = false) String positionTitle,
                                @RequestParam(required = false) String description,
                                @RequestParam(defaultValue = "false") boolean needsItServices,
                                @RequestParam(required = false) String itComment) {

        Employee targetEmployee;

        // ВАРІАНТ 1: Оформлення абсолютно нового працівника
        if ("NEW_EMPLOYEE".equals(requestType)) {
            Department department = departmentRepository.findByNameIgnoreCase(departmentName.trim())
                    .orElseGet(() -> {
                        Department d = new Department();
                        d.setName(departmentName.trim());
                        return departmentRepository.save(d);
                    });

            Position position = positionRepository.findByTitleIgnoreCase(positionTitle.trim())
                    .orElseGet(() -> {
                        Position p = new Position();
                        p.setTitle(positionTitle.trim());
                        return positionRepository.save(p);
                    });

            Employee newEmployee = new Employee();
            newEmployee.setFirstName(firstName);
            newEmployee.setLastName(lastName);
            newEmployee.setMiddleName(middleName);
            newEmployee.setEmail(email);
            newEmployee.setPhone(phone);
            newEmployee.setHireDate(LocalDate.now());
            newEmployee.setDepartment(department);
            newEmployee.setPosition(position);

            employeeService.saveEmployee(newEmployee);
            targetEmployee = newEmployee;
            requestType = "Прийом на роботу";

            // ВАРІАНТ 2: Розумне редагування даних існуючого працівника
        } else if ("EDIT_EMPLOYEE".equals(requestType)) {
            if (existingEmployeeId == null) throw new IllegalArgumentException("Не обрано працівника!");

            // Завантажуємо поточний стан з бази
            Employee currentEmployee = employeeService.getAllEmployees().stream()
                    .filter(e -> e.getId().equals(existingEmployeeId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Працівника не знайдено"));

            // Перевіряємо чи змінилися СУТТЄВІ поля
            String currentDept = currentEmployee.getDepartment() != null ? currentEmployee.getDepartment().getName() : "";
            String currentPos = currentEmployee.getPosition() != null ? currentEmployee.getPosition().getTitle() : "";

            boolean isDeptChanged = !currentDept.equalsIgnoreCase(departmentName.trim());
            boolean isPosChanged = !currentPos.equalsIgnoreCase(positionTitle.trim());

            // Оновлюємо базові несуттєві дані миттєво
            currentEmployee.setFirstName(firstName.trim());
            currentEmployee.setLastName(lastName.trim());
            currentEmployee.setMiddleName(middleName != null ? middleName.trim() : null);
            currentEmployee.setEmail(email.trim());
            currentEmployee.setPhone(phone.trim());

            employeeService.saveEmployee(currentEmployee);
            targetEmployee = currentEmployee;

            // 🔥 УМОВА ЗМІНЕНА: Якщо міняється відділ, посада АБО потрібне IT (needsItServices == true)
            if (isDeptChanged || isPosChanged || needsItServices) {
                HRRequest editRequest = new HRRequest();

                // Якщо міняється посада/відділ — це кадрове переведення, якщо тільки IT — запит на ресурси
                if (isDeptChanged || isPosChanged) {
                    editRequest.setRequestType("Зміна посади / відділу");
                } else {
                    editRequest.setRequestType("Запит IT-ресурсів (Редагування)");
                }

                editRequest.setStatus("НА_РОЗГЛЯДІ_КЕРІВНИКА");
                editRequest.setCreatedDate(LocalDate.now());
                editRequest.setEmployee(currentEmployee);

                // Формуємо опис змін
                StringBuilder changeLog = new StringBuilder();
                if (isDeptChanged || isPosChanged) {
                    changeLog.append(String.format("Кадрове переведення. Старий відділ був: '%s', тепер Новий відділ: '%s'. Стара посада була: '%s', тепер Нова посада: '%s'",
                            currentDept, departmentName.trim(), currentPos, positionTitle.trim()));
                } else {
                    changeLog.append("Дані профілю оновлено. Надіслано запит на додаткове IT-забезпечення. ");
                }

                if (description != null && !description.isBlank()) {
                    changeLog.append("Коментар: ").append(description.trim());
                }
                editRequest.setDescription(changeLog.toString());

                // Формуємо коментар для IT
                editRequest.setComment("Потребує послуг IT: " + (needsItServices ? "Так" : "Ні")
                        + (needsItServices && itComment != null && !itComment.isBlank() ? ". Завдання: " + itComment.trim() : ""));

                // ЗБЕРІГАЄМО ЗАЯВКУ В БАЗУ
                hrRequestRepository.save(editRequest);

                return "redirect:/";
            }

            // Якщо зміни були суто косметичні (тільки номер телефону поміняли), заявку не створюємо, просто повертаємо на головну
            return "redirect:/";

            // ВАРІАНТ 3: Звичайні заявки (Відпустки, Лікарняні)
        } else {
            if (existingEmployeeId == null) throw new IllegalArgumentException("Не обрано працівника!");
            targetEmployee = employeeService.getAllEmployees().stream()
                    .filter(e -> e.getId().equals(existingEmployeeId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Працівника не знайдено"));
        }

        // СТВОРЕННЯ СТАНДАРТНОЇ ЗАЯВКИ (для Варіанту 1 та Варіанту 3)
        HRRequest hrRequest = new HRRequest();
        hrRequest.setRequestType(requestType);
        hrRequest.setStatus("НА_РОЗГЛЯДІ_КЕРІВНИКА");
        hrRequest.setCreatedDate(LocalDate.now());
        hrRequest.setEmployee(targetEmployee);

        if (description != null && !description.isBlank()) {
            hrRequest.setDescription(description.trim());
        }

        StringBuilder itBuilder = new StringBuilder();
        itBuilder.append("Потребує послуг IT: ").append(needsItServices ? "Так" : "Ні");
        if (needsItServices && itComment != null && !itComment.isBlank()) {
            itBuilder.append(". Що потрібно: ").append(itComment.trim());
        }
        hrRequest.setComment(itBuilder.toString());

        hrRequestRepository.save(hrRequest);

        return "redirect:/";
    }

    // 4. СТАРЕ ЗБЕРЕЖЕННЯ (Залишаємо про всяк випадок, якщо десь викличеться)
    @PostMapping("/employees/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/";
    }

    // 5. ЗМІНА СТАТУСУ ЗАЯВКИ (Оновлено: автоматичне кадрове переведення та звільнення)
    @PostMapping("/requests/{id}/update-status")
    public String updateRequestStatus(@PathVariable("id") Long requestId, @RequestParam("status") String status) {
        HRRequest request = hrRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Невідомий ID заявки: " + requestId));

        String finalStatus = status;

        // Якщо заявку повністю погоджено / завершено керівником або IT
        if ("ЗАВЕРШЕНО".equals(status)) {
            Employee employee = request.getEmployee();

            if (employee != null) {
                String type = request.getRequestType();

                // СЦЕНАРІЙ 1: ЗВІЛЬНЕННЯ (переводимо в 99-й відділ)
                if ("Звільнення".equalsIgnoreCase(type)) {
                    Department archiveDept = departmentRepository.findById(99L).orElse(null);
                    if (archiveDept != null) {
                        employee.setDepartment(archiveDept);
                        employeeService.saveEmployee(employee);
                    }
                }

                // СЦЕНАРІЙ 2: КАДРОВЕ ПЕРЕВЕДЕННЯ (Зміна посади / відділу)
                else if ("Зміна посади / відділу".equalsIgnoreCase(type)) {
                    // Парсимо текст опису заявки, щоб дізнатися, які нові назви туди ввів кадровик.
                    // Текст має вигляд: "... Новий відділ: 'IT' ... Нова посада: 'Python Developer' ..."
                    String description = request.getDescription();

                    if (description != null) {
                        try {
                            // Витягуємо назву відділу між одинарними дужками '...'
                            String deptName = description.split("Новий відділ: '")[1].split("'")[0].trim();
                            // Витягуємо назву посади між одинарними дужками '...'
                            String posTitle = description.split("Нова посада: '")[1].split("'")[0].trim();

                            // 1. Шукаємо або створюємо новий відділ
                            Department department = departmentRepository.findByNameIgnoreCase(deptName)
                                    .orElseGet(() -> {
                                        Department d = new Department();
                                        d.setName(deptName);
                                        return departmentRepository.save(d);
                                    });

                            // 2. Шукаємо або створюємо нову посаду
                            Position position = positionRepository.findByTitleIgnoreCase(posTitle)
                                    .orElseGet(() -> {
                                        Position p = new Position();
                                        p.setTitle(posTitle);
                                        return positionRepository.save(p);
                                    });

                            // 3. Присвоюємо оновлені об'єкти працівнику та зберігаємо в базу
                            employee.setDepartment(department);
                            employee.setPosition(position);
                            employeeService.saveEmployee(employee);

                        } catch (Exception e) {
                            System.err.println("Помилка автоматичного парсингу даних для кадрового переведення: " + e.getMessage());
                            // Якщо парсинг впаде через зміну формату тексту, додаток не ляже, а просто запише помилку
                        }
                    }
                }
            }
        }

        request.setStatus(finalStatus);
        hrRequestRepository.save(request);

        return "redirect:/";
    }

    @GetMapping("/api/employees/{id}")
    @ResponseBody
    public Employee getEmployeeById(@PathVariable("id") Long id) {
        return employeeService.getAllEmployees().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Працівника з ID " + id + " не знайдено"));
    }

    // АПІ ДЛЯ ДОДАВАННЯ ТЕХНІКИ ПРАЦІВНИКУ
    @PostMapping("/api/equipment/add")
    @ResponseBody
    public String addEquipmentToEmployee(@RequestParam Long employeeId,
                                         @RequestParam String itemName,
                                         @RequestParam String serialNumber,
                                         @RequestParam(required = false) String comment) {

        Employee employee = employeeService.getAllEmployees().stream()
                .filter(e -> e.getId().equals(employeeId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Працівника не знайдено"));

        Equipment eq = new Equipment();
        eq.setItemName(itemName.trim());
        eq.setSerialNumber(serialNumber.trim());
        eq.setComment(comment != null ? comment.trim() : "");
        eq.setEmployee(employee);

        equipmentRepository.save(eq); // Переконайся, що у тебе інжектнутий equipmentRepository
        return "{\"status\":\"success\"}";
    }

    // АПІ ДЛЯ ВИДАЛЕННЯ (СПИСАННЯ) ТЕХНІКИ
    @PostMapping("/api/equipment/delete/{id}")
    @ResponseBody
    public String deleteEquipment(@PathVariable Long id) {
        equipmentRepository.deleteByIdDirectly(id);

        return "{\"status\":\"success\"}";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Повертає шаблон login.html з папки templates
    }
}
