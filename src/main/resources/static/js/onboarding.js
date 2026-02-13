document.addEventListener("DOMContentLoaded", () => {

    // Map modal IDs to backend enum values
    const modalCategoryMap = {
        parent: "PARENTS",
        specialsomeone: "SPECIAL_SOMEONE"
    };

    // Store fetched Q&A per modal
    let activeAuth = {
        question: null,
        answer: null,
        modalId: null
    };

    // ── Step 1: When a modal opens ──
    document.querySelectorAll(".modal").forEach(modal => {
        modal.addEventListener("show.bs.modal", () => {

            const modalId = modal.id;
            const category = modalCategoryMap[modalId];
            localStorage.setItem("category", modalCategoryMap[modalId]);

            if (!category) return;

            activeAuth.modalId = modalId;

            fetch("/api/admin/authenticate/send", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ category })
            })
            .then(res => {
                if (!res.ok) throw new Error("Request failed");
                return res.json();
            })
            .then(data => {
                if (!data || data.length === 0) {
                    alert("No authentication questions found for this category.");
                    return;
                }

                const qa = data[0];
                activeAuth.question = qa.question;
                activeAuth.answer = qa.answer;

                modal.querySelector(".form-floating label").textContent = qa.question;
                modal.querySelector("input[name='answer']").value = "";
            })
            .catch(err => {
                console.error("Failed to fetch authentication question:", err);
                alert("Something went wrong while fetching the question.");
            });
        });
    });

    // ── Step 2: Validate answer when primary button is clicked ──
    document.querySelectorAll(".modal .btn-primary").forEach(button => {
        button.addEventListener("click", () => {

            const modal = button.closest(".modal");
            const input = modal.querySelector("input[name='answer']");
            const userAnswer = input.value.trim();

            if (!userAnswer) {
                alert("Please enter your answer.");
                return;
            }

            if (!activeAuth.question) {
                alert("No question loaded. Please reopen the modal.");
                return;
            }

            fetch("/api/admin/authenticate/validate", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    question: activeAuth.question,
                    answer: userAnswer
                })
            })
            .then(res => {
                if (!res.ok) throw new Error("Validation failed");
                window.location.href = "/view/main-panel.html";
            })
            .catch(err => {
                console.error("Validation failed:", err);
                alert("Incorrect answer. Try again!");
            });
        });
    });

    // ── Cancel button closes modal ──
    document.querySelectorAll(".modal .btn-secondary").forEach(button => {
        button.addEventListener("click", () => {
            const modal = button.closest(".modal");
            const instance = bootstrap.Modal.getInstance(modal);
            instance?.hide();
        });
    });

    // ── Reset state when modal closes ──
    document.querySelectorAll(".modal").forEach(modal => {
        modal.addEventListener("hidden.bs.modal", () => {

            activeAuth.question = null;
            activeAuth.answer = null;
            activeAuth.modalId = null;

            modal.querySelector("input[name='answer']").value = "";
            modal.querySelector(".form-floating label").textContent = "Question";
        });
    });

    //allows for the redirection of peers-belonged visitors to the main panel without authentication
    const peerBtn = document.getElementById("peer-btn");
    if (peerBtn) {
        peerBtn.addEventListener("click", () => {
            localStorage.setItem("category", "PEERS");
            window.location.href = "/view/main-panel.html?category=PEERS";
        })
    }
});
