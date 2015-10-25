ogifyApp.controller('ReturnHomeController', function ($rootScope, $scope, uiGmapGoogleMapApi) {

    $scope.returnHome = function() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                $rootScope.map.center = {
                    latitude: position.coords.latitude,
                    longitude: position.coords.longitude
                };
                $rootScope.selfMarker.coords = {
                    latitude: position.coords.latitude,
                    longitude: position.coords.longitude
                };
                $rootScope.$apply();
            });
        } else {
            console.log("Geolocation is not supported by this browser.");
        }
    };
});
