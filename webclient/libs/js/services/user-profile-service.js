/**
 * Created by melge on 04.11.2015.
 */
ogifyApp.service('orderAddress', function () {
    var address = {
        plainAddress: ''
    };
    return {
        getAddress: function () {
            return address;
        },
        setAddress: function(textAddress) {
            address.plainAddress = textAddress;
        }
    };
});

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
