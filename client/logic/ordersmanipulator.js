var pos;
var map;
var orders;
var myOrders;


function setPositionAndInit(pos, map){
    this.map = map;
    this.pos = pos;
    if(localStorage.getItem('myOrdersList')){
        myOrders = JSON.parse(localStorage.getItem('myOrdersList'));
        showMyOrders();
    }
    orders = getOrders();


}


function showOerdersOnMap(){

}

function getOrders(){
    $.ajax({
        type: "GET",
        url: '/rest/orders',
        data: pos,
        success: function(result) {

        },
        error: function() {
            console.warn("Problem with orders occured");

        }
    });
}

function showMyOrders(){

}