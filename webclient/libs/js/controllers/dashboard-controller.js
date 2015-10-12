/**
 * Created by melge on 11.10.2015.
 */

ogifyApp.controller('DashboardController', function ($rootScope, $scope, uiGmapGoogleMapApi,
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

    $scope.ordersLinks = {};
    $scope.showingOrders = Order.getMyOrders().$promise.then(function() {
        $scope.getOrdersLinks();
    });

    $scope.selfMarker = {
        coords  : { latitude: 55.7, longitude: 37.6 },
        id: "currentPosition"
    };
    $scope.current_active = "my";
    $scope.pageSize = 7;
    $scope.pagesInBar = 9;

    $scope.$on('createdNewOrderEvent', function(event, order) {
        $scope.showingOrders.push(order);
    });

    var goToMyOrders = function () {
        Order.getMyOrders().$promise.then(function (data) {
            $scope.currentUserOrders = data;
            $scope.showingOrders = data;
            $scope.totalPages = window.Math.ceil(data.length / $scope.pageSize);
            $scope.currentActive = "my";
            $scope.page = 0;
            $scope.getOrdersLinks();
            if ($scope.totalPages < $scope.pagesInBar) {
                $scope.pages = _.range($scope.totalPages);
            } else {
                $scope.pages = _.range($scope.pagesInBar);
            }
        });
    };

    var goToNearOrders = function () {
        Order.getNearMe($scope.map.center).$promise.then(function (data) {
            $scope.currentUserOrders = data;
            $scope.showingOrders = data;
            $scope.totalPages = window.Math.ceil(data.length / $scope.pageSize);
            $scope.currentActive = "near";
            $scope.page = 0;
            $scope.getOrdersLinks();
            if ($scope.totalPages < $scope.pagesInBar) {
                $scope.pages = _.range($scope.totalPages);
            } else {
                $scope.pages = _.range($scope.pagesInBar);
            }
        });
    };

    goToMyOrders();

    $scope.setClickedOrder = function(order){
        ClickedOrder.set(order);
    };

    $scope.previousPage = function(){
        if ($scope.page > 0) {
            $scope.page -= 1;
            if ($scope.page + 1 == $scope.pages[0]) {
                Math = window.Math;
                $scope.pages = _.range(Math.floor($scope.page / $scope.pagesInBar),
                    Math.min(Math.floor($scope.page / $scope.pagesInBar)+$scope.pagesInBar,
                        $scope.totalPages));
            }
        }
    };

    $scope.nextPage = function(){
        if ($scope.page < $scope.totalPages - 1) {
            $scope.page += 1;
            if ($scope.page - 1 == $scope.pages[$scope.pages.length-1]) {
                $scope.pages = _.range($scope.page,
                    window.Math.min($scope.page + $scope.pagesInBar, $scope.totalPages));
            }
        }
    };

    $scope.setPage = function(i){
        $scope.page = i;
    };

    $scope.orderGroups = [{
        name: 'near',
        value: 'Все заказы',
        orderViewModeChanged: goToNearOrders
    }, {
        name: 'my',
        value: 'Мои заказы',
        orderViewModeChanged:goToMyOrders
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
});
