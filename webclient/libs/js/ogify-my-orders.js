ogifyApp.controller('MyOrdersController', function ($scope, Order, ClickedOrder) {
    $scope.myOrders = Order.getMyOrders();
    $scope.doneOrders = Order.getDoneOrders();

    $scope.setClickedOrder = function(order) {
        ClickedOrder.set(order);
    };

    $scope.onlyNew = function(order) {
        return order.status == 'New';
    }

    $scope.onlyRunning = function(order) {
        return order.status == 'Running';
    }

    $scope.onlyDone = function(order) {
        return order.status == 'Done';
    }

});
