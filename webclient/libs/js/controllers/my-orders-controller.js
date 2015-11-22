ogifyApp.controller('MyOrdersController', function ($scope, UserProfile, ClickedOrder, UserProfileService) {
    // TODO: put user profile into service, to avoid server spamming.
    $scope.user = UserProfileService.getUserProfile();

    $scope.user.$promise.then(function () {
        $scope.myOrders = UserProfile.getCreatedOrders({userId: $scope.user.userId});
    });

    UserProfileService.forceUpdate();
    $scope.unratedOrders = UserProfileService.getUnratedOrders();
    $scope.$on('unratedOrdersUpdated', function(event, data) {
        $scope.unratedOrders = UserProfileService.getUnratedOrders();
    });

    $scope.maxDescriptionLength = 50;
    $scope.maxAddressLength = 20;

    $scope.setClickedOrder = function(order) {
        ClickedOrder.set(order);
    };

    $scope.setClickedOrderRate = function(order) {
        ClickedOrder.setWithRate(order);
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
