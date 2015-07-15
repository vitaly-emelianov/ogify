/**
 * Created by melge on 12.07.2015.
 */

var BASE_PATH = '/rest';
var AUTH_PATH = '/auth';
var PROFILE_PATH = '/user';

var authServices = angular.module('ogifyServices', ['ngResource']);

authServices.factory('UserProfile', ['$resource',
    function($resource) {
        return $resource(BASE_PATH + PROFILE_PATH, {}, {
            getCurrentUser: {method: 'GET', params: {}, isArray: false}
        });
    }]);



authServices.controller('NavBarController', function($scope, $resource, UserProfile) {
    /**
     * Resource for call to getRequestUri method on server.
     *
     * Social network (sn) must be provided, or vk will be used instead.
     * @type {Object}
     */
    var authRequestUri = $resource(BASE_PATH + AUTH_PATH + '/getRequestUri', {}, {
        getVkUri: {method: 'GET', params: {sn: 'vk'}},
        getFacebookUri: {method: 'GET', params: {sn: 'facebook'}}
    });

    /**
     * Resource for call to auth method on server.
     *
     * Code from social network and state must be provided.
     * @type {Object}
     */
    var auth = $resource(BASE_PATH + AUTH_PATH, {});

    var isAuthenticated = $resource(BASE_PATH + AUTH_PATH + '/isAuthenticated', {});


    $scope.isAuthenticated = isAuthenticated.get({}, function () {
        $scope.isAuthenticated.lastStatus = true;
    }, function () {
        $scope.isAuthenticated.lastStatus = false;
    });

    $scope.requestUri = authRequestUri;
    $scope.requestUri.getVkUri({}, function(uriObject) {
        $scope.requestUri.vkUri = uriObject.requestUri;
    });
    $scope.requestUri.getFacebookUri({}, function(uriObject) {
        $scope.requestUri.facebookUri = uriObject.requestUri;
    });

    $scope.user = UserProfile.getCurrentUser({}, function() {
        $scope.user.loaded = true; });
});

