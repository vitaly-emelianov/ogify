ogifyApp.controller('DashboardController', function ($rootScope, $scope, $filter, uiGmapGoogleMapApi,
                                                     Order, myAddress, ClickedOrder) {
    $scope.getOrdersLinks = function() {
        var showingOrdersIds = [];

        $scope.showingOrders.forEach(function(order, i, arr) {
            showingOrdersIds.push(order.id);
        });

        if(showingOrdersIds.length > 0) {
            $scope.ordersLinks = Order.getOrdersLinks({ordersIds: showingOrdersIds});
        }
    };
    
    $scope.selfMarker = {
        coords  : { latitude: 55.7, longitude: 37.6 },
        id: "currentPosition"
    };
    
    var getMaxOrdersInPage = function() {
        return 5;
    }
    
    var getMaxDescription = function() {
        return 50;
    }
    
    var getMaxPagesInBar = function() {
        return 9;
    }
    
    $scope.pageParameters = {
        pageSize: getMaxOrdersInPage(),
        pagesInBar: getMaxPagesInBar(),
        descriptionLength: getMaxDescription()
    }

    $scope.$on('createdNewOrderEvent', function(event, order) {
        if ($scope.currentActive == "my") {
            $scope.showingOrders.push(order);
            $scope.totalPages = window.Math.ceil($scope.showingOrders.length / $scope.pageParameters.pageSize);
        }
    });

    var switchToMyOrders = function() {
        Order.getMyOrders().$promise.then(function(data){
            $scope.currentUserOrders = data;
            $scope.showingOrders = data;
            $scope.totalPages = window.Math.ceil(data.length / $scope.pageParameters.pageSize);
            $scope.currentActive = "my";
            $scope.currentPage = {
                page: 0,
                pages: _.range(window.Math.min($scope.totalPages, $scope.pageParameters.pagesInBar))
            }
        });
    }
    
    var switchToNearOrders = function(){
        Order.getNearMe($scope.map.center).$promise.then(function(data){
            $scope.showingOrders = data;
            $scope.getOrdersLinks();
            $scope.totalPages = window.Math.ceil(data.length / $scope.pageParameters.pageSize);
            $scope.currentActive = "near";
            $scope.currentPage = {
                page: 0,
                pages: _.range(window.Math.min($scope.totalPages, $scope.pageParameters.pagesInBar))
            }
        });
    }

    switchToMyOrders();

    $scope.setClickedOrder = function(order){
        ClickedOrder.set(order);
    };

    $scope.previousPage = function(currentPage){
        if (currentPage.page > 0) {
            currentPage.page -= 1;
            if (currentPage.page + 1 == currentPage.pages[0]) {
                Math = window.Math;
                currentPage.pages = _.range(Math.floor(currentPage.page / $scope.pageParameters.pagesInBar), 
                                       Math.min(Math.floor(currentPage.page / $scope.pageParameters.pagesInBar)
                                                           +$scope.pageParameters.pagesInBar,
                                                $scope.totalPages));
            }
        }
    };

    $scope.nextPage = function(currentPage){
        if (currentPage.page < $scope.totalPages - 1) {
            currentPage.page += 1;
            if (currentPage.page - 1 == currentPage.pages[currentPage.pages.length-1]) {
                currentPage.pages = _.range(currentPage.page,
                                       window.Math.min(currentPage.page + $scope.pageParameters.pagesInBar, $scope.totalPages));
            }
        };
    };

    $scope.setPage = function(currentPage, i){
        currentPage.page = i;
    };

    $scope.orderGroups = [{
        name: 'near',
        value: 'Все заказы',
        orderViewModeChanged: switchToNearOrders
    }, {
        name: 'my',
        value: 'Мои заказы',
        orderViewModeChanged: switchToMyOrders
    }];

    $rootScope.map = {
        center: { latitude: 55.7, longitude: 37.6 },
        zoom: 10,
        control: {}
    };

    uiGmapGoogleMapApi.then(function(maps) {
        $scope.maps = maps;
        if(!!navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                $scope.map.center = { latitude: position.coords.latitude, longitude: position.coords.longitude };

                var geocoder = new google.maps.Geocoder();
                var myposition = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                geocoder.geocode({'latLng': myposition},function(data, status) {
                    if(status == google.maps.GeocoderStatus.OK) {
                        myAddress.setAddress(
                            data[0].formatted_address,
                            position.coords.latitude,
                            position.coords.longitude
                        );
                    }
                });

                $scope.map.control.refresh($scope.map.center);
                $scope.map.zoom = 11;

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
                    events: {
                        dragend: function (marker, eventName, args) {
                            var latitude = marker.getPosition().lat();
                            var longitude = marker.getPosition().lng();
                            var geocoder = new google.maps.Geocoder();
                            var myposition = new google.maps.LatLng(latitude, longitude);
                            geocoder.geocode({'latLng': myposition},function(data,status) {
                                if(status == google.maps.GeocoderStatus.OK) {
                                    myAddress.setAddress(
                                        data[0].formatted_address,
                                        latitude,
                                        longitude
                                    );
                                }
                            });
                        }
                    },
                    id: "currentPosition"
                };
                $scope.selfMarker = selfMarker;
            });
        }
    });
    
    $scope.getExpireDate = function(order) {
        return $filter('date')(order.expireIn, 'd MMMM yyyy HH:mm');
    };
});
