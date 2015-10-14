ogifyApp.controller('MyOrdersController', function ($scope, Order, ClickedOrder) {

    $scope.myOrders = Order.getMyOrders();
    $scope.maxDescriptionLength = 50;
    $scope.maxAddressLength = 20;

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

    $scope.$on('createdNewOrderEvent', function(event, order) {
        $scope.myOrders.push(order);
    });

});
