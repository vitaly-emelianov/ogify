/**
 * Created by melge on 12.07.2015.
 */

var ogifyApp = angular.module('ogifyApp', ['ogifyServices', 'ngRoute', 'ngCookies', 'uiGmapgoogle-maps']);

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

ogifyApp.controller('NavBarController', function ($scope, $window, $cookies, AuthResource, UserProfile, Order) {

    $scope.modalWindowTemplateUri = 'templates/navbar/auth-modal.html';

    //$scope.authenticationStatus = AuthResource.authenticationStatus();

    $scope.authVk = function () {
        AuthResource.getVkUri(function (data) {
            $window.location.href = data.requestUri;
        });
    };

    $scope.logoutSN = function () {
        $cookies.remove("JSESSIONID");
        $cookies.remove("ogifySessionSecret");
        $cookies.remove("sID");

        $window.location.reload();
    };

    $scope.createOrder = function() {
        var neworder = {
            svekla : 'heyhey',
            morkov : 'nounou'
        };
        Order.create(neworder);
    };

    $scope.user = UserProfile.getCurrentUser();
});

ogifyApp.controller('DashboardController', function ($rootScope, $scope, uiGmapGoogleMapApi, Order) {
    $scope.currentUserOrders = Order.query();

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

            });
        }
    });
});

ogifyApp.controller('CreateOrderModalController', function ($scope) {

});