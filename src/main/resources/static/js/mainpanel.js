document.addEventListener("DOMContentLoaded", () => {

    const category = localStorage.getItem("category");

    if (!category) {
        console.error("No category found in localStorage. Redirecting...");
        window.location.href = "/view/onboarding-page.html";
        return;
    }

    const container = document.getElementById("container");

    fetch("/api/admin/letter/display", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ category: category })
    })
    .then(res => {
        if (!res.ok) throw new Error("Failed to fetch letters");
        return res.json();
    })
    .then(data => {
        container.innerHTML = "";

        data.forEach(item => {
            const paper = document.createElement("div");
            paper.classList.add("paper");
            paper.textContent = item.text;
            container.appendChild(paper);
        });
    })
    .catch(err => console.error("Error loading letters:", err));
})