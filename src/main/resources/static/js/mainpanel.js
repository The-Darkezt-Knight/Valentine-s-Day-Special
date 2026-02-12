document.addEventListener("DOMContentLoaded", () => {

    const paper = document.getElementById("paper");

    fetch("/api/admin/letter/display", {
        method: "POST",
        headers: { "Content-Type": "application/json" }
    })
    .then(res => res.json())
    .then(data => {
        paper.innerHTML = "";

        data.forEach(item => {
            const container = document.createElement("div");
            container.classList.add("letter");
            container.textContent = item;
            paper.appendChild(container);
        });
    })
    .catch(err => console.error(err));

});
