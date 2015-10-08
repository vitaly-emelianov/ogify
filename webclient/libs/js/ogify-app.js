/**
 * Created by melge on 12.07.2015.
 */

var ogifyApp = angular.module('ogifyApp', ['ogifyServices', 'ngRoute', 'ngCookies', 'uiGmapgoogle-maps']);

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

ogifyApp.run(function ($rootScope, $http) {
    $rootScope.navBarTemplateUri = 'templates/navbar/navbar.html';
    $rootScope.createOrderTemplateUri = 'templates/new-order.html';
    $rootScope.showOrderTemplateUri = 'templates/order-details.html'

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

ogifyApp.controller('NavBarController', function ($scope, $window, $cookies, AuthResource, UserProfile) {

    $scope.modalWindowTemplateUri = 'templates/navbar/auth-modal.html';

    //$scope.authenticationStatus = AuthResource.authenticationStatus();

    $scope.authVk = function () {
        AuthResource.getVkUri(function (data) {
            $window.location.href = data.requestUri;
        });
    };

    $scope.logoutSN = function () {
        cookiesPath = {path : "/"};
        $cookies.remove("JSESSIONID", cookiesPath);
        $cookies.remove("ogifySessionSecret", cookiesPath);
        $cookies.remove("sId", cookiesPath);

        $window.location.reload();
    };

    $scope.updateOrderData = function() {
    };

    $scope.user = UserProfile.getCurrentUser();
});

ogifyApp.controller('DashboardController', function ($rootScope, $scope, uiGmapGoogleMapApi,
                                                     Order, myAddress, ClickedOrder) {
    $scope.pageSize = 7;
    $scope.pagesInBar = 9;
    
    goToMyOrders = function() {
        Order.getMyOrders().$promise.then(function(data){
            $scope.currentUserOrders = data;
            $scope.showingOrders = data;
            $scope.totalPages = window.Math.ceil(data.length / $scope.pageSize);
            $scope.currentActive = "my";
            $scope.page = 0;
            if ($scope.totalPages < $scope.pagesInBar){
                $scope.pages = _.range($scope.totalPages);
            } else {
                $scope.pages = _.range($scope.pagesInBar);
            }
        });
    }
    
    goToNearOrders = function(){
        Order.getNearMe($scope.map.center).$promise.then(function(data){
            $scope.currentUserOrders = data;
            $scope.showingOrders = data;
            $scope.totalPages = window.Math.ceil(data.length / $scope.pageSize);
            $scope.currentActive = "near";
            $scope.page = 0;
            if ($scope.totalPages < $scope.pagesInBar){
                $scope.pages = _.range($scope.totalPages);
            } else {
                $scope.pages = _.range($scope.pagesInBar);
            }
        });
    }
    
    goToMyOrders();
    
    $scope.setClickedOrder = function(order){
        ClickedOrder.set(order);
    };
    
    $scope.previousPage = function(){
        if ($scope.page > 0) {
            $scope.page -= 1;
            if ($scope.page + 1 == $scope.pages[0]) {
                Math = window.Math;
                $scope.pages = _.range(Math.floor($scope.page / $scope.pagesInBar), 
                                       Math.min(Math.floor($scope.page / $scope.pagesInBar)+$scope.pagesInBar,
                                           $scope.totalPages));
            }
        }
    };
    
    $scope.nextPage = function(){
        if ($scope.page < $scope.totalPages - 1) {
            $scope.page += 1;
            if ($scope.page - 1 == $scope.pages[$scope.pages.length-1]) {
                $scope.pages = _.range($scope.page,
                                       window.Math.min($scope.page + $scope.pagesInBar, $scope.totalPages));
            }
        };
    };
    
    $scope.setPage = function(i){
        $scope.page = i;
    };
    
    $scope.orderGroups = [{
        name: 'near',
        value: 'Все заказы',
        orderViewModeChanged: goToNearOrders
    }, {
        name: 'my',
        value: 'Мои заказы',
        orderViewModeChanged:goToMyOrders
    }];

    $rootScope.map = {
        center: { latitude: 55.7, longitude: 37.6 },
        zoom: 10,
        control: {}
    };

    uiGmapGoogleMapApi.then(function(maps) {
        $scope.maps = maps;
        if(navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                $scope.map.center = { latitude: position.coords.latitude, longitude: position.coords.longitude };

                var geocoder = new google.maps.Geocoder();
                var myposition = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                geocoder.geocode({'latLng': myposition},function(data, status) {
                    if(status == google.maps.GeocoderStatus.OK) {
                        myAddress.setAddress(
                            data[0].formatted_address,
                            position.coords.latitude,
                            position.coords.longitude
                        );
                    }
                });

                $scope.map.control.refresh($scope.map.center);
                $scope.map.zoom = 11;

                //personal marker init
                $scope.selfMarker = {
                    options: {
                        draggable: true,
                        animation: google.maps.Animation.DROP,
                        icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
                    },
                    coords: {
                        latitude: position.coords.latitude,
                        longitude: position.coords.longitude
                    },
                    events: {
                        dragend: function (marker, eventName, args) {
                            var latitude = marker.getPosition().lat();
                            var longitude = marker.getPosition().lng();

                            var geocoder = new google.maps.Geocoder();
                            var myposition = new google.maps.LatLng(latitude, longitude);
                            geocoder.geocode({'latLng': myposition},function(data,status) {
                                if(status == google.maps.GeocoderStatus.OK) {
                                    myAddress.setAddress(
                                        data[0].formatted_address,
                                        latitude,
                                        longitude
                                    );
                                }
                            });
                        }
                    },
                    id: "currentPosition"
                };
            });
        }
    });
});

ogifyApp.controller('CreateOrderModalController', function ($rootScope, $scope, $filter, Order, myAddress) {
    $scope.order = {
        expireDate: $filter('date')(new Date(), 'dd.MM.yyyy'),
        expireTime: $filter('date')(new Date(), 'hh:mm'),
        reward: '',
        address: myAddress.getAddress(),
        namespace: 'FriendsOfFriends',
        description:'',
        items: [{}]
    };

    $scope.chooseTime = function() {
        var input = angular.element('#expire_in_time').clockpicker();
        input.clockpicker('show');
    };

    $scope.addToList = function() {
        $scope.order.items.push({});
    };

    $scope.createOrder = function() {
        Order.create({
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
        }, function(successResponse) { // success
            angular.element('#createOrderModal').modal('hide');
            
            $scope.order = {
                expireDate: $filter('date')(new Date(), 'dd.MM.yyyy'),
                expireTime: $filter('date')(new Date(), 'hh:mm'),
                reward: '',
                address: myAddress.getAddress(),
                namespace: 'FriendsOfFriends',
                description:'',
                items: [{}]
            };
        }, function(errorResponse) { // error
            // TODO: Add error handler
        });
    };

});

ogifyApp.factory('ClickedOrder', function(){
    var ClickedOrder = {};
    ClickedOrder.order = {description: null, reward: null, address: null, expireIn: null};
    ClickedOrder.set = function(order){
        ClickedOrder.order = order;
    };
    return ClickedOrder;
});

ogifyApp.controller('ShowOrderModalController', function ($scope, ClickedOrder, Order) {
    $scope.getDescription = function(){
        return ClickedOrder.order.description;
    };
    $scope.getOwnerName = function(){
        return ClickedOrder.order.owner.fullName;
    };
    $scope.getOwnerPhotoUrl = function(){
        return ClickedOrder.order.owner.photoUri;
    };
    $scope.getAddress = function(){
        return ClickedOrder.order.address;
    };
    $scope.getReward = function(){
        return ClickedOrder.order.reward;
    };
    $scope.getExpireDate = function(){
        var date = new Date(ClickedOrder.order.expireIn);
        var months = ["января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", 
                      "сентября", "октября", "ноября", "декабря"];
        return [date.getDate(), months[date.getMonth()], date.getFullYear()].join(' ');
    };
    $scope.getExpireTime = function(){
        function toTwoDigital(number) {
            if (number < 10) {
                number = "0" + number;
            }
            return number;
        }
        var date = new Date(ClickedOrder.order.expireIn);
        return [toTwoDigital(date.getHours()), toTwoDigital(date.getMinutes())].join(':');
    };
});
