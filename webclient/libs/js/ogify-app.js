/**
 * Created by melge on 12.07.2015.
 */
var ogifyApp = angular.module('ogifyApp', ['ogifyServices', 'ngRoute', 'ngCookies', 'uiGmapgoogle-maps', 'angulartics',
    'angulartics.google.analytics', 'ngSanitize']);

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
    
    //init self position
    if(!!navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            selfMarker = {
                coords: {
                    latitude: position.coords.latitude,
                    longitude: position.coords.longitude
                }
            };
            $rootScope.selfMarker = selfMarker;
            $rootScope.$apply();
        });
    } else {
        selfMarker = {
            coords: { latitude: 55.927106, longitude: 37.523662 }
        };
        $rootScope.selfMarker = selfMarker;
        $rootScope.$apply();
    }
    
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
        } else {
            $timeout.cancel(timeoutPromise);
            waitingDialog.hide();
        }
    });
});



