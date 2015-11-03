/**
 * Created by melge on 12.07.2015.
 */

ogifyApp.config(function ($routeProvider, uiGmapGoogleMapApiProvider) {
    $routeProvider
        .when('/current', {
            templateUrl: 'templates/current.html'
        }).when('/dashboard', {
            templateUrl: 'templates/dashboard.html',
            controller: 'DashboardController'
        }).when('/profile/:userId', {
            templateUrl: 'templates/user-profile.html',
            controller: 'ProfilePageController'
        }).when('/my-orders', {
            templateUrl: 'templates/my-orders.html',
            controller: 'MyOrdersController'
        }).when('/in-progress', {
            templateUrl: 'templates/in-progress.html',
            controller: 'DashboardController'
        }).otherwise({
            redirectTo: '/dashboard'
        });

    uiGmapGoogleMapApiProvider.configure({
        key: 'AIzaSyB3JGdwrXd_unNoKWm8wLWzWO2NTjMZuHA',
        v: '3.17',
        libraries: 'weather,geometry,visualization',
        language: 'ru'
    });
});

ogifyApp.run(function ($rootScope, $http, $cookies, $window, $timeout) {
    $rootScope.navBarTemplateUri = 'templates/navbar/navbar.html';
    $rootScope.modalWindowsUri = 'templates/modals/modals.html';
    $rootScope.createOrderModalUri = 'templates/modals/new-order.html';
    $rootScope.showOrderModalUri = 'templates/modals/order-details.html';
    $rootScope.rateDoneOrderModalUri = 'templates/modals/rate-done-order.html';
    $rootScope.landingUri = '/landing';

    /* Will be fixed in new version of Bootstrap (Angular.js Bootrstap bug) */
    $rootScope.$on('$locationChangeStart', function(event) {
        angular.element('#authModal').modal('hide');
        angular.element('#createOrderModal').modal('hide');
        angular.element('#showOrderModal').modal('hide');
        angular.element('#rateDoneOrder').modal('hide');
    });

    if(($cookies.get('sId') == undefined || $cookies.get('ogifySessionSecret') == undefined)
        && $window.location.hostname != 'localhost') {
        $window.location.replace($rootScope.landingUri);
    }

    var timeoutPromise;

    $rootScope.$watch(function () {
        return $http.pendingRequests.length > 0;
    }, function (v) {
        if (v) {

            timeoutPromise = $timeout(waitingDialog.show, 1500);
            //waitingDialog.show();
        } else {
            $timeout.cancel(timeoutPromise);
            waitingDialog.hide();
        }
    });
});

ogifyApp.controller('NavBarController', function ($scope, $rootScope, $window, $cookies, $location, AuthResource, UserProfile) {

    $scope.authWindowModalUri = 'templates/modals/auth-modal.html';

    //$scope.authenticationStatus = AuthResource.authenticationStatus();

    $scope.authVk = function () {
        AuthResource.getVkUri(function (data) {
            $window.location.href = data.requestUri;
        });
    };

    $scope.logoutSN = function () {
        var cookiesPath = {path: "/"};
        $cookies.remove("JSESSIONID", cookiesPath);
        $cookies.remove("ogifySessionSecret", cookiesPath);
        $cookies.remove("sId", cookiesPath);

        $window.location.reload();
    };

    $scope.updateOrderData = function() {
        $rootScope.$broadcast('updateOrderAddress');
    };

    $scope.user = UserProfile.getCurrentUser();
    
    $scope.getClass = function (partOfPath) {
        if ($location.path().indexOf(partOfPath) > -1) {
            return 'active';
        } else {
            return '';
        }
    }
});

ogifyApp.controller('CreateOrderModalController', function ($rootScope, $scope, $filter, Order,
                                                            orderAddress, uiGmapGoogleMapApi) {
                                                                
    uiGmapGoogleMapApi.then(function(maps) {
        $scope.maps = maps;
        var geocoder = new google.maps.Geocoder();
        
        // Init telephone input
        $scope.telephoneInput = angular.element("#telephoneNumber");
        $scope.telephoneInput.intlTelInput({
            defaultCountry: "ru",
            preferredCountries: ["ru", "by", "ua"]
        });
        
        $scope.telephoneInput.blur(function () {
            if(this.value.length < 1) {
                this.classList.remove('iti-invalid-key');
                return;
            }

            var isNumberValid = $scope.telephoneInput.intlTelInput("isValidNumber");
            if(!isNumberValid) {
                this.classList.add('iti-invalid-key');
            } else {
                this.classList.remove('iti-invalid-key');
            }
        });

        $scope.telephoneInput.keyup(function(){
            var isNumberValid = $scope.telephoneInput.intlTelInput("isValidNumber");
            var error = $scope.telephoneInput.intlTelInput("getValidationError");
            if(!isNumberValid &&
                (error == intlTelInputUtils.validationError.TOO_LONG
                || error == intlTelInputUtils.validationError.IS_POSSIBLE)) {
                this.classList.add('iti-invalid-key');
            } else {
                this.classList.remove('iti-invalid-key');
            }
        });
        
        initOrder = function() {
            $scope.order = {
                expireDate: $filter('date')(new Date(), 'dd.MM.yyyy'),
                expireTime: $filter('date')(new Date(), 'H:MM'),
                reward: '',
                address: {coords: $rootScope.selfMarker.coords,
                          addressField: orderAddress.getAddress()},
                namespace: 'FriendsOfFriends',
                description:'',
                items: [{}]
            }
        }
        
        initOrder();
        
        $scope.$on('updateOrderAddress', function() {
            $scope.order.address.coords = $rootScope.selfMarker.coords;
            var myposition = new google.maps.LatLng($rootScope.selfMarker.coords.latitude, $rootScope.selfMarker.coords.longitude);
            geocoder.geocode({'latLng': myposition},function(data,status) {
                if(status == google.maps.GeocoderStatus.OK) {
                    orderAddress.setAddress(
                        data[0].formatted_address
                    );
                    $scope.$apply();
                }
            });
        });
        
        $scope.alerts = {warning: [], error: []};

        $scope.showAlert = function(message, type) {
            var alert = {message: message};
            $scope.alerts[type] = [alert];
        };

        $scope.hideAlert = function() {
            $scope.alerts.warning = [];
            $scope.alerts.error = [];
        };

        $scope.chooseTime = function() {
            var input = angular.element('#expire_in_time').clockpicker();
            input.clockpicker('show');
        };

        $scope.addToList = function() {
            $scope.order.items.push({});
        };

        $scope.itemInList = function(index) {
            return (index != $scope.order.items.length - 1);
        };

        $scope.removeFromList = function(index) {
            $scope.order.items.splice(index, 1);
        };

        $scope.showSuggestedAddresses = false;
        $scope.suggestedAddresses = [];
        
        $scope.updateAddressCoordinates = function() {
            geocoder.geocode({'address': $scope.order.address.addressField.plainAddress},function(data, status) {
                if(status == google.maps.GeocoderStatus.OK) {
                   $scope.suggestedAddresses = data;
                   $scope.showSuggestedAddresses = true;
                   $scope.$apply();
                }
            });
        }
        
        $scope.setOrderAddress = function(suggestedAddress){
            orderAddress.setAddress(suggestedAddress.formatted_address);
            $scope.order.address.coords.latitude = suggestedAddress.geometry.location.lat();
            $scope.order.address.coords.longitude = suggestedAddress.geometry.location.lng();
            $scope.showSuggestedAddresses = false;
        }
        
        $scope.createOrder = function() {
            var last_item_index = $scope.order.items.length - 1;
            var $last_item = $scope.order.items[last_item_index];
            if(!$last_item.comment) {
                $scope.order.items.splice(last_item_index, 1);
            }
                
            if ($scope.showSuggestedAddresses == true) {
                $scope.setOrderAddress($scope.suggestedAddresses[0]);
                $scope.showSuggestedAddresses = false;
            }
        
            var newOrder = {
                items: $scope.order.items,
                expireIn: parseDate($scope.order.expireDate, $scope.order.expireTime).getTime(),
                latitude:  $scope.order.address.coords.latitude,
                longitude: $scope.order.address.coords.longitude,
                reward: $scope.order.reward,
                telephoneNumber: $scope.telephoneInput[0].value.length > 0 ?
                    $scope.telephoneInput.intlTelInput("getNumber") : null,
                status: 'New',
                owner: null,
                executor: null,
                address: $scope.order.address.addressField.plainAddress,
                doneAt: null,
                id: null,
                createdAt: null,
                namespace: $scope.order.namespace,
                description: $scope.order.description
            };

            var MAX_TEXT_SIZE = 200;
            var restrictions = [
                {
                    isAppearing: newOrder.description.length > MAX_TEXT_SIZE,
                    message: "Слишком длинное описание заказа"
                },
                {
                    isAppearing: newOrder.reward.length > MAX_TEXT_SIZE,
                    message: "Слишком длинное описание вознаграждения"
                },
                {
                    isAppearing: newOrder.address.length > MAX_TEXT_SIZE,
                    message: "Слишком длинный адрес"
                },
                {
                    isAppearing: !$scope.telephoneInput.intlTelInput("isValidNumber")
                    && $scope.telephoneInput[0].value.length > 0,
                    message: "Некорректный номер телефона"
                }
            ];

            for (var i in restrictions) {
                if (restrictions[i].isAppearing) {
                    $scope.showAlert(restrictions[i].message, 'warning');
                    return;
                }
            }
            newOrder = Order.create(newOrder,
                function(successResponse) {
                    angular.element('#createOrderModal').modal('hide');
                    $scope.hideAlert();
                    $rootScope.$broadcast('createdNewOrderEvent', newOrder);
                    initOrder();
                },
                function(errorResponse) {
                    $scope.showAlert("Неизвестная техническая ошибка: попробуйте позже", 'error');
                }
            );
        };
    });
});

ogifyApp.factory('ClickedOrder', function() {
    var ClickedOrder = {};
    ClickedOrder.order = {
        description: null,
        reward: null,
        items: [],
        address: null,
        expireIn: null,
        owner: {photoUri: null, fullName: null},
        executor: {photoUri: null, fullName: null}
    };
    
    ClickedOrder.set = function(order) {
        ClickedOrder.order = order;
        ClickedOrder.rate = false;
    };
    ClickedOrder.setWithRate = function(order, rate) {
        ClickedOrder.order = order;
        ClickedOrder.rate = rate;
    };
    
    return ClickedOrder;
});

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
    $scope.getReward = function() {
        return ClickedOrder.order.reward;
    };
    $scope.getStatus = function() {
        return ClickedOrder.order.status;
    };
    $scope.getRate = function() {
        return ClickedOrder.rate;
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

ogifyApp.controller('rateDoneOrderController', function ($scope, $rootScope, $filter, ClickedOrder, Order) {
    $scope.rateCurrentOrder = function(rating) {
        Order.rateOrder({orderId: ClickedOrder.order.id}, {rate: rating} , function(successResponse) {
                angular.element('#rateDoneOrder').modal('hide');
            },
            function(errorResponse) {
        });
    };
});