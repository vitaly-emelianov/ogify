/**
 * Created by melge on 22.10.2015.
 */

ogifyApp.service('UserProfileService', function ($interval, UserProfile) {
    var currentUser = UserProfile.getCurrentUser();
    var unratedOrdersCache = [];

    $interval(function() {
        currentUser.then(function(user) {
            unratedOrdersCache = UserProfile.getUnratedOrders({userId: user.userId});
        }, 60 * 2 * 1000);
    });

    return {
        getUserProfile: function() {
            return currentUser;
        },
        getUnratedOrders: function() {
            return unratedOrdersCache
        }
    }
});
