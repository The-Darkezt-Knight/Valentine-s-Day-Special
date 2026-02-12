    $(document).ready(function () {

        // Map modal IDs to backend enum values
        const modalCategoryMap = {
            "parent": "PARENTS",
            "specialsomeone": "SPECIAL_SOMEONE"
        };

        // Store fetched Q&A per modal so we can validate later
        let activeAuth = {
            question: null,
            answer: null,
            modalId: null
        };

        // ── Step 1: When a modal opens, send the category to the backend ──
        $(".modal").on("show.bs.modal", function () {
            const modalId = $(this).attr("id");
            const category = modalCategoryMap[modalId];

            if (!category) return;

            activeAuth.modalId = modalId;

            $.ajax({
                url: "/api/admin/authenticate/send",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify({ category: category }),
                success: function (data) {
                    if (!data || data.length === 0) {
                        alert("No authentication questions found for this category.");
                        return;
                    }

                    // Pick the first question from the list
                    const qa = data[0];
                    activeAuth.question = qa.question;
                    activeAuth.answer = qa.answer;

                    // Display the question in the modal's label
                    const modal = $("#" + modalId);
                    modal.find(".form-floating label").text(qa.question);
                    modal.find("input[name='answer']").val("");
                },
                error: function (xhr) {
                    console.error("Failed to fetch authentication question:", xhr.responseText);
                    alert("Something went wrong while fetching the question.");
                }
            });
        });

        // ── Step 2: When "Read my letters" is clicked, validate the answer ──
        $(".modal .btn-primary").on("click", function () {
            const modal = $(this).closest(".modal");
            const userAnswer = modal.find("input[name='answer']").val().trim();

            if (!userAnswer) {
                alert("Please enter your answer.");
                return;
            }

            if (!activeAuth.question) {
                alert("No question loaded. Please reopen the modal.");
                return;
            }

            $.ajax({
                url: "/api/admin/authenticate/validate",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                    question: activeAuth.question,
                    answer: userAnswer
                }),
                success: function () {
                    // Validation passed – redirect to the main panel
                    window.location.href = "/view/main-panel.html";
                },
                error: function (xhr) {
                    console.error("Validation failed:", xhr.responseText);
                    alert("Incorrect answer. Try again!");
                }
            });
        });

        // ── Cancel button dismisses the modal ──
        $(".modal .btn-secondary").on("click", function () {
            const modal = $(this).closest(".modal");
            bootstrap.Modal.getInstance(modal[0])?.hide();
        });

        // ── Reset state when a modal is closed ──
        $(".modal").on("hidden.bs.modal", function () {
            activeAuth.question = null;
            activeAuth.answer = null;
            activeAuth.modalId = null;
            $(this).find("input[name='answer']").val("");
            $(this).find(".form-floating label").text("Question");
        });
    });
