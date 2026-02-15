document.addEventListener('DOMContentLoaded', ()=> {

    const send_btn = document.getElementById("send-btn");
    const offcanvas_body = document.getElementById("panel-body");

    let stompClient = null;

    // Append a message element to the chat panel
    function appendMessage(msg) {
        const container = document.createElement("div");
        const p = document.createElement("p");

        container.classList.add("alert", "alert-danger");
        p.textContent = msg.message;

        if (msg.sender) {
            const small = document.createElement("small");
            small.classList.add("text-muted");
            small.textContent = "— " + msg.sender;
            container.appendChild(p);
            container.appendChild(small);
        } else {
            container.appendChild(p);
        }

        offcanvas_body.appendChild(container);
        offcanvas_body.scrollTop = offcanvas_body.scrollHeight;
    }

    // Load existing messages via REST then connect WebSocket
    fetch("/api/public/message/display")
        .then(res => res.json())
        .then(data => {
            data.forEach(message => appendMessage(message));
        })
        .catch(err => {
            console.error("Failed to load messages:", err);
        });

    // Connect to WebSocket via SockJS + STOMP
    function connect() {
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.debug = null; // suppress debug logs

        stompClient.connect({}, function () {
            // Subscribe to the broadcast topic for new messages
            stompClient.subscribe('/topic/messages', function (response) {
                const msg = JSON.parse(response.body);
                appendMessage(msg);
            });
        }, function (error) {
            console.error("WebSocket connection error:", error);
            // Attempt to reconnect after 5 seconds
            setTimeout(connect, 5000);
        });
    }

    connect();

    // Send message over WebSocket
    send_btn.addEventListener('click', (e)=> {
        e.preventDefault();

        const messageInput = document.getElementById("message-input");
        const message = messageInput.value;

        if(!message || message.trim() === "") {
            console.log("Message is empty");
            return;
        }

        if (stompClient && stompClient.connected) {
            stompClient.send("/app/message/send", {},
                JSON.stringify({ message: message })
            );
            messageInput.value = "";
        } else {
            console.error("WebSocket is not connected");
        }
    });
})