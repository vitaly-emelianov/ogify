/**
 * Created by melges on 04.10.15.
 */
ogifyApp.service('currentUserProfile', function() {
    var profile = {};

});

ogifyApp.controller('ProfilePageController', function ($scope, UserProfile) {
    $scope.currentUserProfile = UserProfile.get();
});