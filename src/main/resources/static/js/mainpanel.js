document.addEventListener("DOMContentLoaded", ()=> {

    const category = localStorage.getItem("category")

    const container = document.getElementById("container");

    fetch("/api/admin/letter/display", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            category:category
        })
    })
    .then(res => res.json())
    .then(data => {
        paper.innerHTML = "";

        data.forEach(item => {
            const paper = document.createElement("div");
            paper.classList.add("letter");
            paper.textContent = item.text;
            paper.classList.add("paper");
            container.appendChild(paper);
        });
    })
    .catch(err => console.log(err));
})