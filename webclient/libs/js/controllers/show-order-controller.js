/**
 * Created by melge on 07.11.2015.
 */

ogifyApp.controller('ShowOrderModalController', function ($scope, $rootScope, $filter, UserProfile, ClickedOrder, Order, $interval) {
    $scope.user = UserProfile.getCurrentUser();
    
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
    $scope.getDescription = function() {
        return ClickedOrder.order.description;
    };
    $scope.getItemList = function() {
        return ClickedOrder.order.items;
    };
    $scope.itemsEmpty = function() {
        return (ClickedOrder.order.items.length == 0);
    };
    $scope.getOwnerName = function() {
        return ClickedOrder.order.owner.fullName;
    };
    $scope.getExecutorName = function() {
        return ClickedOrder.order.executor.fullName;
    };
    $scope.getOwnerPhotoUrl = function() {
        return ClickedOrder.order.owner.photoUri;
    };
    $scope.getExecutorPhotoUrl = function() {
        return ClickedOrder.order.executor.photoUri;
    };
    $scope.getAddress = function() {
        return ClickedOrder.order.address;
    };
    $scope.getTelephoneNumber = function() {
            return ClickedOrder.order.telephoneNumber;
    };
    $scope.getReward = function() {
        return ClickedOrder.order.reward;
    };
    $scope.getStatus = function() {
        return ClickedOrder.order.status;
    };
    $scope.getRate = function() {
        return ClickedOrder.rate;
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
}).directive('myCurrentTime', ['$interval', 'dateFilter',
    function($interval, dateFilter) {
        // return the directive link function. (compile function not needed)
        return function(scope, element, attrs) {
            var stopTime; // so that we can cancel the time updates

            // used to update the UI
            function updateTime() {
                element.text(dateFilter(new Date()));
            }

            // watch the expression, and update the UI on change.
            scope.$watch(attrs.myCurrentTime, function(value) {
                updateTime();
            });

            stopTime = $interval(updateTime, 1000);

            // listen on DOM destroy (removal) event, and cancel the next UI update
            // to prevent updating time after the DOM element was removed.
            element.on('$destroy', function() {
                $interval.cancel(stopTime);
            });
        }
    }]);
