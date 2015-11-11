ogifyApp.controller('DashboardController', function ($rootScope, $scope, $filter, uiGmapGoogleMapApi,
                                                     $location, Order, orderAddress, ClickedOrder,
                                                     UserProfile) {
    $scope.user = UserProfile.get();
    
    $rootScope.selfMarker = {
        coords  : { latitude: 55.927106, longitude: 37.523662 },
        id: "currentPosition",
        visible: false
    };

    $scope.markersEvents = {
        click: function(marker, eventName, model){
            $scope.setClickedOrder(model);
            $("#showOrderModal").modal();
        }
    };
    
    var mapChanged = function(map) {
        var bounds = map.getBounds();
        $rootScope.map.bounds.neLatitude = bounds.getNorthEast().lat();
        $rootScope.map.bounds.neLongitude = bounds.getNorthEast().lng();
        $rootScope.map.bounds.swLatitude = bounds.getSouthWest().lat();
        $rootScope.map.bounds.swLongitude = bounds.getSouthWest().lng();

        updateOrders();
    };

    $rootScope.map = {
        center: { latitude: 55.753836, longitude: 37.620463 },
        zoom: 10,
        bounds: {
            neLatitude: 55.95,
            neLongitude: 37.82,
            swLatitude: 55.56,
            swLongitude: 37.42
        },
        control: {},
        events: {
            zoom_changed: function (map) {
                $scope.doSpider = (map.getZoom() > 16);
            },
            dragend: mapChanged,
            idle: mapChanged
        }
    };

    $scope.clusterOptions = {
        gridSize: 60,
        ignoreHidden: true,
        minimumClusterSize: 2,
        maxZoom: 16
    };

    $scope.spiderOptions = {
        keepSpiderfied: true
    };

    $scope.doSpider = false;

    var getMaxOrdersInPage = function() {
        return Math.floor(Math.max((angular.element('.list-orders-height').height()
            - 2*angular.element('.row').height()) / (angular.element('#hidden-order').height()
            + angular.element('.row').height()), 1));
    };
    
    var getMaxPagesInBar = function() {
        return 9;
    };

    if (!$rootScope.pageParameters) {
        $rootScope.pageParameters = {
            pageSize: getMaxOrdersInPage(),
            pagesInBar: getMaxPagesInBar()
        };
    }
    
    $scope.$on('createdNewOrderEvent', function(event, order) {
    });
    $scope.$on('finishOrderEvent', function(event) {
        switchToInProgressOrders();
    });
    $scope.$on('takeOrderEvent', function(event) {
        switchToNearOrders();
    });

    $scope.additionalStyle = {};

    var switchToInProgressOrders = function() {
        $scope.user.$promise.then(function(user) {
            UserProfile.getExecutingOrders({userId: user.userId}).$promise.then(function(data){
                $scope.showingOrders = data;
                
                $scope.totalPages = window.Math.ceil($scope.showingOrders.length / $rootScope.pageParameters.pageSize);
                $scope.currentPage = {
                    page: 0,
                    pages: _.range(window.Math.min($scope.totalPages, $rootScope.pageParameters.pagesInBar))
                };
                
                $scope.showingOrders.forEach(function(elem) {
                    if(isOrderOutdated(elem)) {
                        $scope.additionalStyle[elem.id] = "list-group-item-danger";
                    }
                });
            });
        });
    };
    
    var switchToNearOrders = function(){
        Order.getNearMe($scope.map.bounds).$promise.then(function(data){
            $scope.showingOrders = data.orders;
            $scope.ordersLinks = data.socialLinks;
            $scope.totalPages = window.Math.ceil(data.length / $rootScope.pageParameters.pageSize);
            $scope.currentPage = {
                page: 0,
                pages: _.range(window.Math.min($scope.totalPages, $rootScope.pageParameters.pagesInBar))
            }
        });
    };

    var updateOrders = function() {
        if ($location.path().indexOf('dashboard') > -1) {
            switchToNearOrders();
        } else {
            switchToInProgressOrders();
        }
    };
    updateOrders();

    $scope.setClickedOrder = function(order){
        ClickedOrder.setWithSocialRelationship(order, $scope.ordersLinks[order.id]);
    };
    
    $scope.setClickedOrderWithoutSocialRelationship = function(order){
        ClickedOrder.set(order);
    };
    
    $scope.previousPage = function(currentPage){
        if (currentPage.page > 0) {
            currentPage.page -= 1;
            if (currentPage.page + 1 == currentPage.pages[0]) {
                Math = window.Math;
                currentPage.pages = _.range(Math.floor(currentPage.page / $rootScope.pageParameters.pagesInBar), 
                                       Math.min(Math.floor(currentPage.page / $rootScope.pageParameters.pagesInBar)
                                                           +$rootScope.pageParameters.pagesInBar,
                                                $scope.totalPages));
            }
        }
    };

    $scope.nextPage = function(currentPage){
        if (currentPage.page < $scope.totalPages - 1) {
            currentPage.page += 1;
            if (currentPage.page - 1 == currentPage.pages[currentPage.pages.length-1]) {
                currentPage.pages = _.range(currentPage.page,
                                       window.Math.min(currentPage.page + $rootScope.pageParameters.pagesInBar, $scope.totalPages));
            }
        }
    };

    $scope.setPage = function(currentPage, i){
        currentPage.page = i;
    };

    uiGmapGoogleMapApi.then(function(maps) {
        $rootScope.maps = maps;
        if(!!navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                $rootScope.map.center = { latitude: position.coords.latitude, longitude: position.coords.longitude };
                $rootScope.map.control.refresh($rootScope.map.center);
                $rootScope.map.zoom = 11;

                //personal marker init
                selfMarker = {
                    options: {
                        draggable: true,
                        animation: google.maps.Animation.DROP,
                        icon: 'libs/images/man_marker.png'
                    },
                    coords: {
                        latitude: position.coords.latitude,
                        longitude: position.coords.longitude
                    },
                    id: "currentPosition",
                    visible: true
                };
                $rootScope.selfMarker = selfMarker;
                $rootScope.$apply();
            });
        }
    });
    
    $scope.getExpireDate = function(order) {
        return $filter('date')(order.expireIn, 'd MMMM yyyy HH:mm');
    };
});
