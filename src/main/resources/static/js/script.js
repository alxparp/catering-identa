document.addEventListener("DOMContentLoaded", function() {
    var cart = document.getElementById("cart");
    console.log("call initial products");

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/main/displayCart",
        dataType: "json",
        cache: false,
        timeout: 600000,
        success: function (data) {
            displayCart(data, cart);
        },
    });
});

function addToCart(event, productId) {
    event.preventDefault();
    var products = {};
    products['id'] = productId;

    var cart = document.getElementById("cart");


    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/main/addProductToCart",
        data: JSON.stringify(products),
        dataType: "json",
        cache: false,
        timeout: 600000,
        success: function (data) {
            displayCart(data, cart);
        },
    });
}

function deleteProductFromCart(productId) {
    var products = {};
    products['id'] = productId;

    var cart = document.getElementById("cart");

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/main/deleteFromCart",
        data: JSON.stringify(products),
        dataType: "json",
        cache: false,
        timeout: 600000,
        success: function (data) {
            displayCart(data, cart);
        },
    });

}

function displayCart(data, cart) {
    console.log(data);
    if (data.result.orderItems.length === 0) {
        cart.innerHTML = "";
        return;
    }
    var result = "<h3>Кошик зі стравами</h3>";
    $.each(data.result.orderItems, function (index, value) {
        result += "<div class='cartProduct'>";
        result += "     <div class='cartProductDescription'>";
        result += "         <img src='/img/" + value.productDTO.img + "'>";
        result += "         <a>" + value.productDTO.name + "<br> " +
            "<span> Ціна " + value.productDTO.price + " грн</span> <br> " +
            "<span>Кількість: " + value.quantity + "</span></a>";
        result += "     </div>";
        result += "     <a class='close' onclick='deleteProductFromCart(" + value.productDTO.id + ")'>X</a>";
        result += "</div>";
    });
    result += "<p class='totalPrice'>Разом: <span>" + data.result.sum + " грн</span></p>";
    cart.innerHTML = result;
}