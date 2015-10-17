ogifyApp.controller('MyOrdersController', function ($scope, UserProfile, Order, ClickedOrder) {

    $scope.user = UserProfile.get();
    $scope.myOrders = Order.getMyOrders();

    $scope.maxDescriptionLength = 50;
    $scope.maxAddressLength = 20;

    $scope.setClickedOrder = function(order) {
        ClickedOrder.set(order);
    };

    $scope.createdByMe = function(order) {
        return order.owner.userId == $scope.user.userId;
    }

    $scope.onlyNew = function(order) {
        return order.status == 'New';
    };

    $scope.onlyRunning = function(order) {
        return order.status == 'Running';
    };

    $scope.onlyDone = function(order) {
        return order.status == 'Done';
    };

    $scope.$on('createdNewOrderEvent', function(event, order) {
        $scope.myOrders.push(order);
    });

});

