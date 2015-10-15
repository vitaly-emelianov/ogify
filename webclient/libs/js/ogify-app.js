/**
 * Created by melge on 12.07.2015.
 */

var ogifyApp = angular.module('ogifyApp', ['ogifyServices', 'ngRoute', 'ngCookies', 'uiGmapgoogle-maps', 'angulartics',
    'angulartics.google.analytics']);

ogifyApp.service('myAddress', function () {
    var address = {
        latitude: 0.0,
        longitude: 0.0,
        plainAddress: ''
    };
    return {
        getAddress: function () {
            return address;
        },
        setAddress: function(textAddress, latitude, longitude) {
            address.plainAddress = textAddress;
            address.latitude = latitude;
            address.longitude = longitude;
        }
    };
});

ogifyApp.config(function ($routeProvider, uiGmapGoogleMapApiProvider) {
    $routeProvider
        .when('/current', {
            templateUrl: 'templates/current.html'
        }).when('/dashboard', {
            templateUrl: 'templates/dashboard.html',
            controller: 'DashboardController'
        }).when('/profile', {
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

ogifyApp.run(function ($rootScope, $http, $cookies, $window) {
    $rootScope.navBarTemplateUri = 'templates/navbar/navbar.html';
    $rootScope.createOrderTemplateUri = 'templates/new-order.html';
    $rootScope.showOrderTemplateUri = 'templates/order-details.html'
    $rootScope.landingUri = '/landing';

    if(($cookies.get('sId') == undefined || $cookies.get('ogifySessionSecret') == undefined)
        && $window.location.hostname != 'localhost') {
        $window.location.replace($rootScope.landingUri);
    }

    $rootScope.$watch(function () {
        return $http.pendingRequests.length > 0;
    }, function (v) {
        if (v) {
            waitingDialog.show();
        } else {
            waitingDialog.hide();
        }
    });
});

ogifyApp.controller('NavBarController', function ($scope, $window, $cookies, $location, AuthResource, UserProfile) {

    $scope.modalWindowTemplateUri = 'templates/navbar/auth-modal.html';

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
                                                            myAddress) {
    $scope.order = {
        expireDate: $filter('date')(new Date(), 'dd.MM.yyyy'),
        expireTime: $filter('date')(new Date(), 'hh:mm'),
        reward: '',
        address: myAddress.getAddress(),
        namespace: 'FriendsOfFriends',
        description:'',
        items: [{}]
    };

    $scope.alerts = {warning: [], error: []};

    $scope.showAlert = function(message, type) {
        var alert = {message: message};
        $scope.alerts[type] = [alert];
    };

    $scope.hideAlert = function() {
        $scope.alerts.warning = [];
        $scope.alerts.error = [];
    }

    $scope.chooseTime = function() {
        var input = angular.element('#expire_in_time').clockpicker();
        input.clockpicker('show');
    };

    $scope.addToList = function() {
        $scope.order.items.push({});
    };

    $scope.createOrder = function() {
        var newOrder = {
            items: $scope.order.items,
            expireIn: parseDate($scope.order.expireDate, $scope.order.expireTime).getTime(),
            latitude: myAddress.getAddress().latitude,
            longitude: myAddress.getAddress().longitude,
            reward: $scope.order.reward,
            status: 'New',
            owner: null,
            executor: null,
            address: $scope.order.address.plainAddress,
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
            }
        ];

        for (var i in restrictions) {
            if (i.isAppearing) {
                $scope.showAlert(i.message, 'warning');
                return;
            }
        }

        newOrder = Order.create(newOrder,
            function(successResponse) {
                angular.element('#createOrderModal').modal('hide');
                $scope.hideAlert();
                $scope.order = {
                    expireDate: $filter('date')(new Date(), 'dd.MM.yyyy'),
                    expireTime: $filter('date')(new Date(), 'hh:mm'),
                    reward: '',
                    address: myAddress.getAddress(),
                    namespace: 'FriendsOfFriends',
                    description:'',
                    items: [{}]
                };
                $rootScope.$broadcast('createdNewOrderEvent', newOrder);
            },
            function(errorResponse) {
                $scope.showAlert("Неизвестная техническая ошибка: попробуйте позже", 'error');
            }
        );
    };
});

ogifyApp.factory('ClickedOrder', function() {
    var ClickedOrder = {};
    ClickedOrder.order = {
        description: null,
        reward: null,
        address: null,
        expireIn: null,
        owner: {photoUri: null, fullName: null}
    };
    ClickedOrder.set = function(order) {
        ClickedOrder.order = order;
    };
    return ClickedOrder;
});

ogifyApp.controller('ShowOrderModalController', function ($scope, $filter, ClickedOrder) {
    $scope.getDescription = function() {
        return ClickedOrder.order.description;
    };
    $scope.getOwnerName = function() {
        return ClickedOrder.order.owner.fullName;
    };
    $scope.getOwnerPhotoUrl = function() {
        return ClickedOrder.order.owner.photoUri;
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
    $scope.userTakesTask = function() {
        Order.getToExecution({orderId:ClickedOrder.order.id});
    };
    $scope.orderToDone = function() {
        //TODO
    };
    $scope.getExpireDate = function() {
        return $filter('date')(ClickedOrder.order.expireIn, 'd MMMM yyyy');
    };
    $scope.getExpireTime = function() {
        return $filter('date')(ClickedOrder.order.expireIn, 'HH:mm');
    };
});
