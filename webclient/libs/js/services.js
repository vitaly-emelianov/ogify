/**
 * Created by melge on 22.10.2015.
 */

ogifyApp.service('UserProfileService', function ($interval, $rootScope, UserProfile) {
    var currentUser = UserProfile.getCurrentUser();
    var unratedOrdersCache = [];
    var updateUnratedOrdersCache = function() {
        unratedOrdersCache = UserProfile.getUnratedOrders({userId: currentUser.userId}, null, function () {
            $rootScope.$broadcast('unratedOrdersUpdated');
        });
    };

    currentUser.$promise.then(function (user) {
        unratedOrdersCache = UserProfile.getUnratedOrders({userId: currentUser.userId}, null, function() {
            $interval(updateUnratedOrdersCache, 120000);
            $rootScope.$broadcast('unratedOrdersUpdated');
        });
    });
     // Schedule repeat after 120s

    return {
        getUserProfile: function() {
            return currentUser;
        },
        getUnratedOrders: function() {
            return unratedOrdersCache
        },
        forceUpdate: function() {
            if(currentUser.$promise.$resolved)
                updateUnratedOrdersCache();
        }
    }
});
