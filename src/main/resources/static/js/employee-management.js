document.addEventListener("DOMContentLoaded", function() {
    console.log("REST API та Модальні вікна синхронізовані.");

    // Глобальний клік-слухач
    document.addEventListener('click', function(e) {
        try {
            // 1. Клік на розгортання картки працівника
            const toggleBtn = e.target.closest('.employee-toggle-btn');
            if (toggleBtn) {
                const empId = toggleBtn.getAttribute('data-emp-id');
                const prefix = toggleBtn.getAttribute('data-prefix');

                if (empId && prefix) {
                    const eqContainer = document.getElementById(`equipment-list-${prefix}-${empId}`);
                    const reqContainer = document.getElementById(`requests-list-${prefix}-${empId}`);

                    if (eqContainer) loadEquipmentFromAPI(empId, eqContainer);
                    if (reqContainer) loadRequestsFromAPI(empId, reqContainer, prefix, toggleBtn);
                }
                return;
            }

            // 2. Клік на керування технікою
            const manageBtn = e.target.closest('.manage-eq-btn');
            if (manageBtn) {
                const empId = manageBtn.getAttribute('data-emp-id');
                const empName = manageBtn.getAttribute('data-emp-name');
                if (empId && empName) {
                    document.getElementById('modalEmployeeName').innerText = empName;
                    document.getElementById('modalEmployeeId').value = empId;
                    loadEquipmentToModal(empId);

                    const modalEl = document.getElementById('equipmentModal');
                    bootstrap.Modal.getOrCreateInstance(modalEl).show();
                }
            }
        } catch (err) {
            console.error("Помилка обробки кліку:", err);
        }
    });

    // Додавання техніки через AJAX
    const addForm = document.getElementById('addEquipmentForm');
    if (addForm) {
        addForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const empId = document.getElementById('modalEmployeeId').value;
            const itemName = document.getElementById('eqItemName').value;
            const serialNumber = document.getElementById('eqSerialNumber').value;
            const comment = document.getElementById('eqComment').value;

            const formData = new URLSearchParams();
            formData.append('employeeId', empId);
            formData.append('itemName', itemName);
            formData.append('serialNumber', serialNumber);
            formData.append('comment', comment);

            fetch('/api/equipment/add', { method: 'POST', body: formData })
                .then(res => res.json())
                .then(data => {
                    if(data.status === 'success') {
                        document.getElementById('eqItemName').value = '';
                        document.getElementById('eqSerialNumber').value = '';
                        document.getElementById('eqComment').value = '';
                        loadEquipmentToModal(empId);

                        ['active', 'normal', 'fired'].forEach(p => {
                            const el = document.getElementById(`equipment-list-${p}-${empId}`);
                            if (el) loadEquipmentFromAPI(empId, el);
                        });
                    }
                });
        });
    }
});

function loadEquipmentFromAPI(empId, container) {
    fetch(`/api/employees/${empId}/equipment`)
        .then(res => res.json())
        .then(data => {
            container.innerHTML = '';
            if (!data || data.length === 0) {
                container.innerHTML = '<li class="list-group-item text-muted small py-1 bg-transparent border-0">Техніка відсутня</li>';
                return;
            }
            data.forEach(eq => {
                const li = document.createElement('li');
                li.className = 'list-group-item d-flex justify-content-between align-items-center small py-1 px-2 mb-1 rounded border bg-white';
                li.innerHTML = `
                        <div><strong>${eq.itemName}</strong> <span class="text-muted" style="font-size: 0.75rem;">(${eq.serialNumber})</span></div>
                        <button class="btn btn-sm text-danger p-0 border-0" onclick="deleteEquipmentDirectly(${eq.id}, ${empId})"><i class="bi bi-trash"></i></button>
                    `;
                container.appendChild(li);
            });
        }).catch(e => console.error(e));
}

// Завантаження заявок та логіка кнопки "Показати всі"
function loadRequestsFromAPI(empId, container, prefix, buttonEl) {
    fetch(`/api/employees/${empId}/requests`)
        .then(res => res.json())
        .then(data => {
            container.innerHTML = '';
            if (!data || data.length === 0) {
                container.innerHTML = '<span class="text-muted small">Заявки відсутні.</span>';
                return;
            }

            // Сортуємо: нові зверху
            data.sort((a,b) => b.id - a.id);

            // Відображаємо максимум 3 штуки
            const itemsToDisplay = data.slice(0, 3);

            itemsToDisplay.forEach(req => {
                const card = document.createElement('div');
                card.className = 'mb-2 p-2 bg-white rounded border small shadow-sm';

                let badgeClass = 'badge bg-warning text-dark';
                if (req.status === 'ЗАВЕРШЕНО') badgeClass = 'badge bg-success';
                if (req.status === 'ВІДХИЛЕНО') badgeClass = 'badge bg-danger';
                if (req.status === 'ПОГОДЖЕНО_ОЧІКУЄ_ІТ') badgeClass = 'badge bg-info text-dark';

                let buttons = '';
                if (prefix === 'active') {
                    if (req.status === 'ONLINE' || req.status === 'НА_РОЗГЛЯДІ_КЕРІВНИКА') {
                        buttons = `
                            <div class="text-end mt-1">
                                <button class="btn btn-xs btn-outline-success py-0" onclick="changeRequestStatus(${req.id}, 'ПОГОДЖЕНО_ОЧІКУЄ_ІТ')">Погодити</button>
                                <button class="btn btn-xs btn-outline-danger py-0 ms-1" onclick="changeRequestStatus(${req.id}, 'ВІДХИЛЕНО')">Відхилити</button>
                            </div>`;
                    } else if (req.status === 'ПОГОДЖЕНО_ОЧІКУЄ_ІТ') {
                        buttons = `
                            <div class="text-end mt-1">
                                <button class="btn btn-xs btn-outline-primary py-0" onclick="changeRequestStatus(${req.id}, 'ЗАВЕРШЕНО')">👷 Видати ресурси</button>
                            </div>`;
                    }
                }

                card.innerHTML = `
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="fw-bold text-dark">${req.requestType}</span>
                            <span class="${badgeClass}" style="font-size:0.65rem;">${req.status.replace(/_/g, ' ')}</span>
                        </div>
                        ${req.description ? `<div class="mt-1 bg-light p-1 rounded" style="font-size:0.75rem;"><strong>Суть:</strong> ${req.description}</div>` : ''}
                        ${req.comment ? `<div class="text-secondary mt-1" style="font-size:0.7rem;">⚙️ ${req.comment}</div>` : ''}
                        ${buttons}
                    `;
                container.appendChild(card);
            });

            // ЯКЩО ЗАЯВОК БІЛЬШЕ НІЖ 3 — ПОВЕРТАЄМО КНОПКУ ІСТОРІЇ
            if (data.length > 3) {
                const empName = buttonEl ? buttonEl.innerText.split('\n')[0] : `Працівник #${empId}`;
                const historyBtn = document.createElement('button');
                historyBtn.className = 'btn btn-sm btn-link text-primary p-0 mt-2 fw-bold text-decoration-none d-block';
                historyBtn.innerHTML = `<i class="bi bi-clock-history"></i> 📜 Вся історія заявок (${data.length})`;

                // Обробка кліку на кнопку історії
                historyBtn.addEventListener('click', function(evt) {
                    evt.stopPropagation(); // щоб акордеон випадково не закрився
                    openHistoryModal(empName, data);
                });
                container.appendChild(historyBtn);
            }

        }).catch(e => console.error(e));
}

// Вікно історії
function openHistoryModal(employeeName, allRequests) {
    document.getElementById('modalHistoryEmployeeName').innerText = employeeName;
    const tbody = document.getElementById('modalHistoryTableBody');
    if (!tbody) return;
    tbody.innerHTML = '';

    allRequests.forEach(req => {
        let badgeClass = 'badge bg-warning text-dark';
        if (req.status === 'ЗАВЕРШЕНО') badgeClass = 'badge bg-success';
        if (req.status === 'ВІДХИЛЕНО') badgeClass = 'badge bg-danger';
        if (req.status === 'ПОГОДЖЕНО_ОЧІКУЄ_ІТ') badgeClass = 'badge bg-info text-dark';

        const tr = document.createElement('tr');
        tr.innerHTML = `
                <td><strong>${req.requestType}</strong></td>
                <td class="small">${req.description || req.comment || '—'}</td>
                <td><span class="${badgeClass}">${req.status.replace(/_/g, ' ')}</span></td>
            `;
        tbody.appendChild(tr);
    });

    const modalEl = document.getElementById('historyModal');
    bootstrap.Modal.getOrCreateInstance(modalEl).show();
}

function changeRequestStatus(requestId, statusName) {
    fetch(`/api/employees/requests/${requestId}/status?status=${encodeURIComponent(statusName)}`, { method: 'POST' })
        .then(() => { location.reload(); });
}

function loadEquipmentToModal(empId) {
    const tbody = document.getElementById('modalEquipmentTableBody');
    tbody.innerHTML = '<tr><td colspan="4" class="text-center text-muted">Завантаження...</td></tr>';
    fetch(`/api/employees/${empId}/equipment`)
        .then(res => res.json())
        .then(data => {
            tbody.innerHTML = '';
            if(!data || data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" class="text-center text-muted">Немає техніки</td></tr>';
                return;
            }
            data.forEach(eq => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                        <td><strong>${eq.itemName}</strong></td>
                        <td><code>${eq.serialNumber}</code></td>
                        <td>${eq.comment || '-'}</td>
                        <td class="text-end"><button class="btn btn-sm btn-link text-danger py-0" onclick="deleteEquipmentDirectly(${eq.id}, ${empId}, true)">Списати</button></td>
                    `;
                tbody.appendChild(tr);
            });
        });
}

function deleteEquipmentDirectly(eqId, empId, isFromModal = false) {
    if (confirm("Видалити цей ресурс?")) {
        fetch(`/api/equipment/delete/${eqId}`, { method: 'POST' })
            .then(res => res.json())
            .then(data => {
                if (data.status === 'success') {
                    if (isFromModal) loadEquipmentToModal(empId);
                    ['active', 'normal', 'fired'].forEach(p => {
                        const el = document.getElementById(`equipment-list-${p}-${empId}`);
                        if (el) loadEquipmentFromAPI(empId, el);
                    });
                }
            });
    }
}