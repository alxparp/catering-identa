document.addEventListener("DOMContentLoaded", function() {
    getNotConfirmedOrders();
});

function getNotConfirmedOrders() {
    $.ajax({
        type: "GET",
        url: '/admin/notConfirmedOrders',
        success: function(data) {
            var result = "";
            $.each(data.result, function (index, value) {
                result += "<div class='order'>";
                result += "<h3>Замовлення №" + value.id + "</h3>";
                result += "<table>";
                result += "     <thead>";
                result += "<tr>";
                result += "<th>Страва</th>";
                result += "<th>Сума</th>";
                result += "</tr>";
                result += "</thead>";
                result += "<tbody>";

                $.each(value.orderItems, function (itemIndex, itemValue) {
                    result += "<tr>";
                    result += "<td>";
                    result += "<span>" + itemValue.productDTO.name + "</span>";
                    result += "<span> *" + itemValue.quantity + "</span>";
                    result += "</td>";
                    result += "<td>" + (itemValue.productDTO.price*itemValue.quantity) + " грн</td>";
                    result += "</tr>";
                });

                result += "<tr>";
                result += "<td>Сума</td>";
                result += "<td>" + value.sum + " грн</td>";
                result += "</tr>";

                result += "</tbody>";
                result += "</table>";
                result += "<div class='decision'>";
                result += "<a href='/admin/approveOrder?id=" + value.id + "'>Підтвердити</a>";
                result += "<a href='/admin/declineOrder?id=" + value.id + "'>Відмінити</a>";
                result += "</div>";
                result += "<div class='clear'></div>";
                result += "</div>";
            });

            $('#notConfirmed').html(result);
        }
    });
};

setInterval(getNotConfirmedOrders, 10000);