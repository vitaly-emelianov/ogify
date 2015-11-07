/**
 * Created by melge on 07.11.2015.
 */

ogifyApp.controller('rateDoneOrderController', function ($scope, $rootScope, $filter, ClickedOrder, Order) {
    $scope.rateCurrentOrder = function(rating) {
        Order.rateOrder({orderId: ClickedOrder.order.id}, {rate: rating} , function(successResponse) {
                angular.element('#rateDoneOrder').modal('hide');
            },
            function(errorResponse) {
            });
    };
});
