/**
 * Created by melge on 07.11.2015.
 */

ogifyApp.controller('ShowOrderModalController', function ($scope, $rootScope, $filter, UserProfile,
                                                          ClickedOrder, Order, $interval) {
    $scope.user = UserProfile.getCurrentUser();

    $scope.orderRatingValue = ClickedOrder.getOrderRate();
    $scope.orderRated = ClickedOrder.isOrderRated();
    $scope.$on("ClickedOrderRateResolved", function(event, feedback) {
        if(feedback != null && feedback.rate != null) {
            $scope.orderRated = true;
            $scope.orderRatingValue = feedback.rate;
        } else {
            $scope.orderRated = false;
            $scope.orderRatingValue = 0;
        }
    });
    
    $scope.timer = 60;
    var stop;

    $scope.startTimer = function() {
        // Don't start a new fight if we are already fighting
        if ( angular.isDefined(stop) ) return;

        stop = $interval(function() {
            if ($scope.timer > 0) {
                $scope.timer = $scope.timer - 1;
            } else {
                $scope.stopTimer();
            }
        }, 1000);
    };

    $scope.isOrderOutdated = isOrderOutdated;

    $scope.stopTimer = function() {
        if (angular.isDefined(stop)) {
            $interval.cancel(stop);
            stop = undefined;
        }
    };

    $scope.$on('$destroy', function() {
        // Make sure that the interval is destroyed too
        $scope.stopTimer();
    });

    $scope.getOrder = function() {
        return ClickedOrder.order;
    };

    $scope.getClickedOrder = function() {
        return ClickedOrder;
    };

    $scope.getSocialRelationship = function() {
        return ClickedOrder.socialRelationship;
    };

    $scope.userTakesTask = function() {
        Order.getToExecution({orderId: ClickedOrder.order.id}, function(successResponse) {
                angular.element('#showOrderModal').modal('hide');
                $rootScope.$broadcast('takeOrderEvent');
            },
            function(errorResponse) {

            });
        $scope.startTimer();
    };
    $scope.orderToDone = function() {
        Order.changeStatus({orderId: ClickedOrder.order.id}, 2, function(successResponse) {
                angular.element('#showOrderModal').modal('hide');
                $rootScope.$broadcast('finishOrderEvent');
            },
            function(errorResponse) {
            });
    };
    $scope.cancelOrder = function() {
        Order.denyOrderExecution({orderId: ClickedOrder.order.id}, function(successResponse) {
                angular.element('#showOrderModal').modal('hide');
                $rootScope.$broadcast('finishOrderEvent');
            },
            function(errorResponse) {
            });
    };

    $scope.rateMyOrder = function(rating) {
        Order.rateOrder({orderId: ClickedOrder.order.id}, {rate: rating} , function(successResponse) {
                $scope.orderRated = true;
                $scope.orderRatingValue = rating;
                $rootScope.$broadcast('rateMyOrderEvent', ClickedOrder.order.id);
            },
            function(errorResponse) {
            });
    };
    $scope.getExpireDate = function() {
        return $filter('date')(ClickedOrder.order.expireIn, 'd MMMM yyyy');
    };
    $scope.getExpireTime = function() {
        return $filter('date')(ClickedOrder.order.expireIn, 'HH:mm');
    };
});
