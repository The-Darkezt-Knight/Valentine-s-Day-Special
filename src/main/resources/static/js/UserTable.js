document.addEventListener('DOMContentLoaded', ()=> {

    // Initialize DataTable once and keep a reference
    const table = $('#user-table').DataTable({
        columns: [
            {data: 'firstName'},
            {data: 'lastName'},
            {data: 'age'}
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

    form.addEventListener('submit', (e) => {
        e.preventDefault();

        const data = {
            'firstName' : document.getElementById("firstName").value,
            'lastName' : document.getElementById("lastName").value,
            'age' : document.getElementById("age").value
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
})
