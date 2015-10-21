ogifyApp.controller('MyOrdersController', function ($scope, UserProfile, ClickedOrder, Order) {
    // TODO: put user profile into service, to avoid server spamming.
    $scope.user = UserProfile.get();

    $scope.user.$promise.then(function(user) {
        UserProfile.getCreatedOrders({userId: user.userId}).$promise.then(function(data) {
            $scope.myOrders = [];
            for (var i = 0; i < data.length; ++i) {
                $scope.myOrders.push(data[i]);
                Order.isOrderRated({orderId: data[i].id}).$promise.then(function(response) {
                    $scope.myOrders[i].rate = response;
                });
            }
        });
    });

    $scope.maxDescriptionLength = 50;
    $scope.maxAddressLength = 20;

    $scope.setClickedOrder = function(order) {
        ClickedOrder.set(order);
    };
    
    $scope.setClickedOrderRate = function(order) {
        ClickedOrder.setWithRate(order, order.rate);
    };

    $scope.onlyNew = function(order) {
        return order.status == 'New';
    };

    $scope.onlyRunning = function(order) {
        return order.status == 'Running';
    };

    $scope.onlyDone = function(order) {
        return order.status == 'Completed';
    };

    $scope.$on('createdNewOrderEvent', function(event, order) {
        order.rate = false;
        $scope.myOrders.push(order);
    });
    
});
