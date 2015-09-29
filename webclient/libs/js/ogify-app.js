/**
 * Created by melge on 12.07.2015.
 */

var ogifyApp = angular.module('ogifyApp', ['ogifyServices', 'ngRoute', 'ngCookies', 'uiGmapgoogle-maps']);

ogifyApp.service('my_address', function () {
    var address = {
        plain_address: ''
    }
    return {
        getAddress: function () {
            return address
        },
        setAddress: function(value) {
            address.plain_address = value;
        }
    };
});
    
ogifyApp.config(function ($routeProvider, uiGmapGoogleMapApiProvider) {
    $routeProvider
        .when('/current', {
            templateUrl: 'templtes/current.html'
        }).when('/dashboard', {
            templateUrl: 'templates/dashboard.html',
            controller: 'DashboardController'
        }).otherwise({
            redirectTo: '/dashboard'
        });

    uiGmapGoogleMapApiProvider.configure({
        key: 'AIzaSyB3JGdwrXd_unNoKWm8wLWzWO2NTjMZuHA',
        v: '3.17',
        libraries: 'weather,geometry,visualization'
    });
});

ogifyApp.run(function ($rootScope, $http) {
    $rootScope.navBarTemplateUri = 'templates/navbar/navbar.html';
    $rootScope.createOrderTemplateUri = 'templates/new-order.html'

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

ogifyApp.controller('NavBarController', function ($rootScope, $scope, $window, $cookies, AuthResource, UserProfile, my_address) {

    $scope.modalWindowTemplateUri = 'templates/navbar/auth-modal.html';

    //$scope.authenticationStatus = AuthResource.authenticationStatus();

    $scope.authVk = function () {
        AuthResource.getVkUri(function (data) {
            $window.location.href = data.requestUri;
        });
    };

    $scope.logoutSN = function () {
        $cookies.remove("JSESSIONID", {path:"/"});
        $cookies.remove("ogifySessionSecret", {path:"/"});
        $cookies.remove("sId", {path:"/"});

        $window.location.reload();
    };

    $scope.createOrder = function() {
        var neworder = {
            svekla : 'heyhey',
            morkov : 'nounou'
        };
        Order.create(neworder);
    };
    
    $scope.updateOrderData = function() {
        my_address.setAddress($rootScope.map.center_address);
    };
    
    $scope.user = UserProfile.getCurrentUser();
});

ogifyApp.controller('DashboardController', function ($rootScope, $scope, uiGmapGoogleMapApi, Order) {
    $scope.currentUserOrders = Order.getMyOrders();
    $scope.showingOrders = $scope.currentUserOrders;
    
    $scope.current_active = "my";

    $scope.orderGroups = [{
        name: 'near',
        value: 'Все заказы',
        orderViewModeChanged: function() {
            $scope.current_active = "near";
            $scope.showingOrders = Order.getNearMe($scope.map.center);
        }
    }, {
        name: 'my',
        value: 'Мои заказы',
        orderViewModeChanged: function() {
            $scope.current_active = "my";
            $scope.showingOrders = Order.getMyOrders();
        }
    }];

    // $scope.orderGroups

    $rootScope.map = {
        center: { latitude: 55.7, longitude: 37.6 },
        zoom: 10,
        control: {},
        center_address: ""
    };

    uiGmapGoogleMapApi.then(function(maps) {
        $scope.maps = maps;
        if(navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                $scope.map.center = { latitude: position.coords.latitude, longitude: position.coords.longitude };

                var geocoder = new google.maps.Geocoder();
                var myposition = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                geocoder.geocode({'latLng': myposition},function(data,status) {
                    if(status == google.maps.GeocoderStatus.OK)
                        $scope.map.center_address = data[0].formatted_address; //this is the full address
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
                            var lat = marker.getPosition().lat();
                            var lon = marker.getPosition().lng();

                            var geocoder = new google.maps.Geocoder();
                            var myposition = new google.maps.LatLng(lat, lon);
                            geocoder.geocode({'latLng': myposition},function(data,status) {
                                if(status == google.maps.GeocoderStatus.OK)
                                    $scope.map.center_address = data[0].formatted_address;
                            });
                        }
                    },
                    id: "currentPosition"
                };
            });
        }
    });
});

ogifyApp.controller('CreateOrderModalController', function ($rootScope, $scope, $filter, Order, my_address) {
    $scope.order = {
        expireDate: $filter('date')(new Date(), 'dd.MM.yyyy'),
        expireTime: $filter('date')(new Date(), 'hh:mm'),
        reward: '',
        address: my_address.getAddress(),
        namespace: 'Friends',
        description:''
    };
    
    $scope.chooseTime = function() {
        var input = angular.element('#expire_in_time').clockpicker();
        input.clockpicker('show');
    };
    
    $scope.createOrder = function() {
        Order.create({
            items: [],
            expireIn: parseDate($scope.order.expireDate, $scope.order.expireTime).getTime(),
            latitude: $rootScope.map.center.latitude,
            longitude: $rootScope.map.center.longitude,
            reward: $scope.order.reward,
            status: 'New',
            owner: null,
            executor: null,
            address: $scope.order.address.plain_address,
            doneAt: null,
            id: null,
            createdAt: null,
            namespace: $scope.order.namespace,
            description: $scope.order.description
        }, function(successResponse) { // success
            angular.element('#createOrderModal').modal('hide');
        }, function(errorResponse) { // error
            // TODO: Add error handler
        });
    };

});