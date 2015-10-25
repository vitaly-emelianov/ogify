/**
 * Created by melges on 04.10.15.
 */
ogifyApp.service('currentUserProfile', function() {
    var profile = {};
});

ogifyApp.controller('ProfilePageController', function ($scope, $routeParams, UserProfile, Order, ClickedOrder) {
    $scope.currentUserProfile = UserProfile.get({userId: $routeParams.userId});
    $scope.completedOrders = Order.getNyOrders();

    $scope.setClickedOrder = function(order) {
        ClickedOrder.set(order);
    };

});

