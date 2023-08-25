let stompClient = null;

window.addEventListener("beforeunload", function () {
    disconnectWebSocket();
});

function websocketAdminConnect(username) {
    generalSocketConnect(username, true, Number.MAX_SAFE_INTEGER);
}

function websocketConnect(username) {
    generalSocketConnect(username, false, 60000);
}

function generalSocketConnect(username, flag, timeoutDuration) {
    const socket = new SockJS('/hello');
    stompClient = Stomp.over(socket);

    stompClient.connect({ username: username }, function () {
        console.log('Web Socket is connected');
        stompClient.subscribe('/users/queue/messages', handleSubscription);
        if (!flag) sendMessage();
    });

    installTimeout();

    function installTimeout() {
        const timeout = setTimeout(handleTimeout, timeoutDuration);
        stompClient.disconnectCallback = () => {
            clearTimeout(timeout);
        };
    }

    function handleSubscription(message) {
        console.log("Here is a response " + message.body);
        if (flag) {
            getNotConfirmedOrders();
        } else {
            document.getElementById("orderHandling").innerHTML = 'Взято в обробку замовлення ';
            unsubscribe();
            disconnectWebSocket();
        }
    }

    function handleTimeout() {
        if (stompClient && stompClient.connected) {
            unsubscribe();
            disconnectWebSocket();
            console.log('Operation timed out');
        }
    }
}

function unsubscribe() {
    if (stompClient && stompClient.connected) {
        stompClient.unsubscribe();
    }
}

function disconnectWebSocket() {
    if (stompClient && stompClient.connected) {
        stompClient.disconnect();
        console.log('Web Socket is disconnected');
    }
}

function sendMessage() {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/hello", {}, "admin");
    }
}

function sendAdminMessage(userId) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/hello", {}, userId);
    }
}
