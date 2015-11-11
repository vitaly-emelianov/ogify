/**
 * Created by melge on 07.11.2015.
 */

ogifyApp.controller('NavBarController', function ($scope, $window, $cookies, $location, AuthResource, UserProfile) {

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
