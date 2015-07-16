/**
 * Created by melge on 12.07.2015.
 */

var BASE_PATH = '/rest';
var AUTH_PATH = '/auth';
var PROFILE_PATH = '/user';

var authServices = angular.module('ogifyServices', ['ngResource']);

/**
 * Resource for call to getRequestUri method on server.
 *
 * Social network (sn) must be provided, or vk will be used instead.
 * @type {Object}
 */
authServices.factory('AuthResource', ['$resource',
    function($resource) {
        return $resource(BASE_PATH + AUTH_PATH + '/getRequestUri', {}, {
            getVkUri: {method: 'GET', params: {sn: 'vk'}},
            getFacebookUri: {method: 'GET', params: {sn: 'facebook'}},
            isAuthenticated: {method: 'GET', url: BASE_PATH + AUTH_PATH + '/isAuthenticated', params: {},
                transformResponse: function(data, headersGetter, status) {
                    if(status == 200) {
                        return true;
                    } else if(status == 401) {
                        return false;
                    } else {
                        return null;
                    }
                }}
        });
    }
]);

authServices.factory('UserProfile', ['$resource',
    function($resource) {
        return $resource(BASE_PATH + PROFILE_PATH, {}, {
            getCurrentUser: {method: 'GET', params: {}, isArray: false}
        });
    }
]);


