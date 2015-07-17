/**
 * Created by melge on 12.07.2015.
 */

var ogifyApp = angular.module('ogifyApp', ['ogifyServices']);

ogifyApp.controller('TemplateController', function($scope) {
    $scope.navBarTemplateUri = 'templates/navbar/navbar.html';
});

ogifyApp.controller('NavBarController', function($scope, $resource, AuthResource, UserProfile) {

    $scope.modalWindowTemplateUri = 'templates/navbar/auth-modal.html';

    $scope.authenticationStatus = AuthResource.authenticationStatus();

    $scope.requestUri = AuthResource;
    $scope.requestUri.getVkUri({}, function(uriObject) {
        $scope.requestUri.vkUri = uriObject.requestUri;
    });
    $scope.requestUri.getFacebookUri({}, function(uriObject) {
        $scope.requestUri.facebookUri = uriObject.requestUri;
    });

    $scope.user = UserProfile.getCurrentUser({}, function() {
        $scope.user.loaded = true; });
});