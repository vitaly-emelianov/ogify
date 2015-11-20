/**
 * Created by melges on 04.10.15.
 */
ogifyApp.service('CurrentUserProfile', function() {
    var profile = {};
});

ogifyApp.controller('ProfilePageController', function ($scope, $routeParams, UserProfile, ClickedOrder) {
    $scope.watchingProfile = UserProfile.get({userId: $routeParams.userId});
    $scope.completedOrders = UserProfile.getCompletedByMeOrders({userId: $routeParams.userId});
    $scope.currentUser = UserProfile.getCurrentUser();
    $scope.setClickedOrder = function(order) {
        ClickedOrder.set(order);
    };
});
