document.addEventListener("DOMContentLoaded", function() {
    console.log("Скрипт форми створення заявок ініціалізовано.");

    const requestTypeSelect = document.getElementById('requestTypeSelect');
    const employeeSelect = document.getElementById('employeeSelect');
    const employeeSearchInput = document.getElementById('employeeSearchInput');

    // ==========================================================================
    // СИНХРОНІЗАЦІЯ ТЕМНОЇ ТЕМИ З LOCALSTORAGE
    // ==========================================================================
    const savedTheme = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-bs-theme', savedTheme);

    // ==========================================================================
    // ОБРОБНИКИ ПОДІЙ ТВОЄЇ СТОРІНКИ
    // ==========================================================================
    if (requestTypeSelect) {
        requestTypeSelect.addEventListener('change', handleFormView);
    }

    if (employeeSelect) {
        employeeSelect.addEventListener('change', function() {
            if (requestTypeSelect.value === 'EDIT_EMPLOYEE') {
                fillEmployeeDataForEdit(this.value);
            }
        });
    }

    if (employeeSearchInput) {
        employeeSearchInput.addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase().trim();
            const options = Array.from(employeeSelect.options).filter(opt => opt.value !== "");
            let visibleCount = 0;

            options.forEach(option => {
                const text = option.text.toLowerCase();
                if (text.includes(searchTerm)) {
                    option.style.display = 'block';
                    visibleCount++;
                } else {
                    option.style.display = 'none';
                    if (option.selected) option.selected = false;
                }
            });

            const countDiv = document.getElementById('searchResultCount');
            if (searchTerm !== "") {
                countDiv.innerText = `Знайдено співробітників: ${visibleCount}`;
            } else {
                countDiv.innerText = "";
            }
        });
    }

    function handleFormView() {
        const value = requestTypeSelect.value;
        const searchBlock = document.getElementById('searchEmployeeBlock');
        const existingBlock = document.getElementById('existingEmployeeBlock');
        const newBlock = document.getElementById('newEmployeeBlock');
        const commonBlock = document.getElementById('commonDetailsBlock');
        const h5Title = newBlock.querySelector('h5');

        const inputs = ['lastNameInput', 'firstNameInput', 'emailInput', 'phoneInput', 'deptInput', 'posInput'];

        // Скидаємо пошук при зміні типу операції
        employeeSearchInput.value = '';
        resetEmployeeFilter();

        if (value === 'NEW_EMPLOYEE') {
            searchBlock.classList.add('hidden-block');
            existingBlock.classList.add('hidden-block');
            newBlock.classList.remove('hidden-block');
            commonBlock.classList.remove('hidden-block');
            h5Title.innerHTML = '<i class="bi bi-person-badge"></i> Анкетні дані нового співробітника';

            inputs.forEach(id => {
                const input = document.getElementById(id);
                input.value = '';
                input.removeAttribute('readonly');
                input.setAttribute('required', 'required');
            });
            employeeSelect.removeAttribute('required');
            document.getElementById('itServicesSwitch').checked = true;

        } else if (value === 'EDIT_EMPLOYEE') {
            searchBlock.classList.remove('hidden-block');
            existingBlock.classList.remove('hidden-block');
            newBlock.classList.remove('hidden-block');
            commonBlock.classList.remove('hidden-block');
            h5Title.innerHTML = '<i class="bi bi-pencil-square"></i> Нові (оновлені) дані працівника';

            inputs.forEach(id => {
                document.getElementById(id).setAttribute('required', 'required');
                document.getElementById(id).removeAttribute('readonly');
            });
            employeeSelect.setAttribute('required', 'required');
            document.getElementById('itServicesSwitch').checked = false;

            const selectedEmpId = employeeSelect.value;
            if (selectedEmpId) fillEmployeeDataForEdit(selectedEmpId);

        } else {
            // Звичайні заявки (відпустка, лікарняний тощо)
            searchBlock.classList.remove('hidden-block');
            existingBlock.classList.remove('hidden-block');
            newBlock.classList.add('hidden-block');
            commonBlock.classList.remove('hidden-block');

            inputs.forEach(id => {
                document.getElementById(id).removeAttribute('required');
            });
            employeeSelect.setAttribute('required', 'required');
            document.getElementById('itServicesSwitch').checked = false;
        }
    }

    function resetEmployeeFilter() {
        if (!employeeSelect) return;
        const options = Array.from(employeeSelect.options);
        options.forEach(opt => opt.style.display = 'block');
        document.getElementById('searchResultCount').innerText = "";
    }

    function fillEmployeeDataForEdit(empId) {
        if(!empId) return;

        fetch('/api/employees/' + empId)
            .then(res => {
                if(!res.ok) throw new Error("Працівника не знайдено");
                return res.json();
            })
            .then(emp => {
                document.getElementById('lastNameInput').value = emp.lastName || '';
                document.getElementById('firstNameInput').value = emp.firstName || '';
                document.getElementById('emailInput').value = emp.email || '';
                document.getElementById('phoneInput').value = emp.phone || '';
                document.getElementById('deptInput').value = emp.department ? emp.department.name : '';
                document.getElementById('posInput').value = emp.position ? emp.position.title : '';
            })
            .catch(err => console.error("Помилка автозаповнення: ", err));
    }
});