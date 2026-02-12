document.addEventListener('DOMContentLoaded', ()=> {

    // Initialize DataTable once and keep a reference
    const table = $('#user-table').DataTable({
        columns: [
            {data: 'firstName'},
            {data: 'lastName'},
            {data: 'age'},
            {data: 'category'},

            {
                data: null,
                orderable: false,
                searchable: false,
                render: function (data, type, row) {
                    return `
                        <button
                            class="btn btn-sm btn-warning update-btn"
                            data-id="${row.id}"
                            data-bs-toggle="modal"
                            data-bs-target="#update"
                            >Update</button>
                    `
                }
            },

            {
                data: null,
                orderable: false,
                searchable: false,
                render: function(data, type, row) {
                    return `
                        <button
                            class="btn btn-sm btn-danger delete-btn"
                            data-bs-toggle="modal"
                            data-bs-target="#remove"
                        >Remove</button>
                    `
                }
            }
        ]
    });

    // Fetch and load data into the table
    function display() {
        fetch('/api/admin/list/display')
        .then(res => res.json())
        .then(data => {
            table.clear();
            table.rows.add(data);
            table.draw();
        })
        .catch(err => console.error("Failed to load table data:", err));
    }

    // Initial load
    display();

    // Handle form submission
    const form = document.getElementById("add-person-form");
    const updateForm = document.getElementById("update-person-form");
    const updateInfo = document.getElementById("update-person-information");
    const updateModalEl = document.getElementById("update");
    const updateFirstName = document.getElementById("change-first-name");
    const updateLastName = document.getElementById("change-last-name");
    const updateAge = document.getElementById("change-age");

    const removeConfirmBtn = document.getElementById("delete-btn");
    const removeModalEl = document.getElementById("remove");
    const removePersonName = document.getElementById("remove-person-name");
    let selectedFirstName = null;

    if (updateModalEl && updateInfo) {
        updateModalEl.addEventListener('show.bs.modal', () => {
            updateInfo.textContent = "";
            updateInfo.classList.remove("text-success", "text-danger");
        });
    }

    $('#user-table tbody').on('click', '.update-btn', function () {
        const rowData = table.row($(this).closest('tr')).data();
        if (!rowData) {
            return;
        }
        if (updateFirstName) updateFirstName.value = rowData.firstName ?? "";
        if (updateLastName) updateLastName.value = rowData.lastName ?? "";
        if (updateAge) updateAge.value = rowData.age ?? "";
    });

    $('#user-table tbody').on('click', '.delete-btn', function () {
        const rowData = table.row($(this).closest('tr')).data();
        if (!rowData) {
            return;
        }
        selectedFirstName = rowData.firstName ?? null;
        if (removePersonName) {
            removePersonName.textContent = selectedFirstName
                ? `Remove ${selectedFirstName}?`
                : "Remove this person?";
        }
    });

    if (removeModalEl) {
        removeModalEl.addEventListener('show.bs.modal', () => {
            if (removePersonName && !removePersonName.textContent) {
                removePersonName.textContent = "Remove this person?";
            }
        });
    }

    // Show/hide secret question fields based on category selection
    const categorySelect = document.getElementById("category");
    const secretFields = document.getElementById("secret-fields");

    if (categorySelect && secretFields) {
        categorySelect.addEventListener('change', () => {
            const val = categorySelect.value;
            if (val === 'PARENTS' || val === 'SPECIAL_SOMEONE') {
                secretFields.style.display = '';
                secretFields.classList.add('d-flex');
            } else {
                secretFields.style.display = 'none';
                secretFields.classList.remove('d-flex');
                document.getElementById('secretQuestion').value = '';
                document.getElementById('secretAnswer').value = '';
            }
        });
    }

    form.addEventListener('submit', (e) => {
        e.preventDefault();

        const ageVal = document.getElementById("age").value;
        const data = {
            'firstName'      : document.getElementById("firstName").value,
            'lastName'       : document.getElementById("lastName").value,
            'age'            : ageVal ? parseInt(ageVal, 10) : null,
            'category'       : document.getElementById("category").value,
            'secretQuestion' : document.getElementById("secretQuestion").value || null,
            'secretAnswer'   : document.getElementById("secretAnswer").value || null
        }

        fetch("/api/admin/list/register", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
        .then(res => {
            if (!res.ok) throw new Error(`Server error: ${res.status}`);
            return res.text();  // Backend returns a plain String, not JSON
        })
        .then(message => {
            console.log("success", message);
            alert(message);
            document.getElementById('add-person-form').reset();
            display();  // Reload table AFTER successful registration
        })
        .catch(err => {
            console.error(err);
            alert("Failed to add person: " + err.message);
        });
    });


    //Form that updates information
    updateForm.addEventListener('submit', (e)=> {
        e.preventDefault();

        const data = {
            firstName : document.getElementById("change-first-name").value,
            lastName : document.getElementById("change-last-name").value,
            age : document.getElementById("change-age").value
        }

        fetch('/api/admin/list/update', {
            method : "POST",
            headers: {"Content-Type" : "application/json"},
            body: JSON.stringify(data)
        })
        .then(res => {
            if(!res.ok){throw new Error(`Server error ${res.status}`)}
            return res.text();
        })
        .then(message => {
            console.log("success", message);
            if (updateInfo) {
                updateInfo.classList.remove("text-danger");
                updateInfo.classList.add("text-success");
                updateInfo.textContent = message;
            }
            updateForm.reset();
            alert(message);
            display();
        })
        .catch(
            err => {
                console.log(err);
                if (updateInfo) {
                    updateInfo.classList.remove("text-success");
                    updateInfo.classList.add("text-danger");
                    updateInfo.textContent = `Failed to update due to ${err.message}`;
                }
            }
        )
    })

    // ── Dedicate letter modal ──
    const dedicateForm = document.getElementById('dedicate-letter-form');
    const dedicateRecipient = document.getElementById('dedicate-recipient');
    const dedicateModalEl = document.getElementById('dedicate');

    // Populate recipient dropdown with unique first names from the table
    function populateRecipientDropdown() {
        const data = table.rows().data().toArray();
        const firstNames = [...new Set(data.map(row => row.firstName).filter(Boolean))].sort();

        // Keep only the default disabled option
        dedicateRecipient.innerHTML = '<option value="" disabled selected></option>';

        firstNames.forEach(name => {
            const option = document.createElement('option');
            option.value = name;
            option.textContent = name;
            dedicateRecipient.appendChild(option);
        });
    }

    // Refresh the dropdown every time the modal opens
    if (dedicateModalEl) {
        dedicateModalEl.addEventListener('show.bs.modal', () => {
            populateRecipientDropdown();
        });
    }

    // Handle dedication form submission
    if (dedicateForm) {
        dedicateForm.addEventListener('submit', (e) => {
            e.preventDefault();

            const payload = {
                firstName: dedicateRecipient.value,
                text: document.getElementById('dedicate-text').value
            };

            fetch('/api/admin/letter/dedicate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })
            .then(res => {
                if (!res.ok) throw new Error(`Server error: ${res.status}`);
                return res.text();
            })
            .then(message => {
                alert(message);
                dedicateForm.reset();
                const modalInstance = bootstrap.Modal.getInstance(dedicateModalEl);
                if (modalInstance) modalInstance.hide();
            })
            .catch(err => {
                console.error(err);
                alert('Failed to dedicate letter: ' + err.message);
            });
        });
    }

    //Removes people
    if (removeConfirmBtn) {
        removeConfirmBtn.addEventListener('click', () => {
            if (!selectedFirstName) {
                alert("No person selected to remove.");
                return;
            }

            fetch('/api/admin/list/remove', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ firstName: selectedFirstName })
            })
            .then(res => {
                if (!res.ok) throw new Error(`Server error ${res.status}`);
                return res.text();
            })
            .then(message => {
                alert(message);
                selectedFirstName = null;
                if (removePersonName) removePersonName.textContent = "";
                const modalInstance = bootstrap.Modal.getInstance(removeModalEl);
                if (modalInstance) {
                    modalInstance.hide();
                }
                display();
            })
            .catch(err => {
                console.error(err);
                alert(`Failed to remove due to ${err.message}`);
            });
        });
    }

    
})
