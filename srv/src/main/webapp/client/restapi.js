//var REQUEST_URL = "http://ogify-miptsail.rhcloud.com";

function getOrdersById(id) {
	$.ajax({
        type: "GET",
        url: "/rest/orders/" + id,
        data: {orderId: id},
        success: function(result) {
            console.log(result);
        },
        error: function() {
            /**
             * nothing to do
             */
        }
    });
}

//function createOrder() {
//	var request = '{"id":1,"owner":{"vkId":10497385,"user_id":1,"fullName":"Морген Матвей","photoUri":"http://cs613528.vk.me/v613528385/17fbe/M7rHtuU38cY.jpg","rating_as_customer":3.5,"rating_as_executor":3.5},"executor":{"vkId":10497385,"user_id":1,"fullName":"Морген Матвей","photoUri":"http://cs613528.vk.me/v613528385/17fbe/M7rHtuU38cY.jpg","rating_as_customer":3.5,"rating_as_executor":3.5},"status":"Running","namespace":"All","latitude":55.930542,"longitude":37.526966,"address":"Долгопрудный, Первомайская 34/5","reward":"Чай с печеньками","description":"Тестовый заказ","createdAt":1423975241000,"doneAt":1424552400000,"expireIn":1424552400000,"items":[{"id":2,"expectedCost":50.0,"comment":"Печенюшки"},{"id":3,"expectedCost":50.0,"comment":"Печенюшки"},{"id":4,"expectedCost":50.0,"comment":"Печенюшки"}]}';
//	var order = JSON.parse(request);
//
//	$.ajax({
//        url: '/rest/orders/',
//        type: 'POST',
//        data: JSON.stringify(order),
//        success: function(data) {
//            console.log(data);
//        },
//        error: function() {
//
//        }
//    });
//}

function getOrders(latitude, longitude) {
    var request = {"latitude": latitude, "longitude": longitude};
    //var order = JSON.parse(request);

    $.ajax({
        url: '/rest/orders/',
        type: 'GET',
        data: request,
        success: function(data) {
            console.log(data);
        },
        error: function() {

        }
    });
}

function createNewOrder(order) {
    //{id: 1, owner: null, executor: null, latitude: 1, longitude: 1, address: "qweqweqweqe", reward: "my simple reward", description: "drink vodka", expireIn: new Date().getTime(), items: [{expectedCost: 12, comment: "asd"}]}
    order.executor = null;
    order.status = "New";
    if(!order.namespace){
        order.namespace = "All"; //Private|Friends|FriendsOfFriends|All
    }
    $.ajax({
        url: '/rest/orders/',
        type: 'POST',
        data: JSON.stringify(order),
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        success: function(data) {
            console.log(data);
        },
        error: function() {

        }
    });
}