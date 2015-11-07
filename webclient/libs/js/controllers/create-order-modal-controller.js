/**
 * Created by melge on 07.11.2015.
 */

ogifyApp.controller('CreateOrderModalController', function ($rootScope, $scope, $filter, Order,
                                                            myAddress) {
    // Init telephone input
    $scope.telephoneInput = angular.element("#telephoneNumber");
    $scope.telephoneInput.intlTelInput({
        defaultCountry: "ru",
        preferredCountries: ["ru", "by", "ua"]
    });

    $scope.telephoneInput.blur(function () {
        if(this.value.length < 1) {
            this.classList.remove('iti-invalid-key');
            return;
        }

        var isNumberValid = $scope.telephoneInput.intlTelInput("isValidNumber");
        if(!isNumberValid) {
            this.classList.add('iti-invalid-key');
        } else {
            this.classList.remove('iti-invalid-key');
        }
    });

    $scope.telephoneInput.keyup(function(){
        var isNumberValid = $scope.telephoneInput.intlTelInput("isValidNumber");
        var error = $scope.telephoneInput.intlTelInput("getValidationError");
        if(!isNumberValid &&
            (error == intlTelInputUtils.validationError.TOO_LONG
            || error == intlTelInputUtils.validationError.IS_POSSIBLE)) {
            this.classList.add('iti-invalid-key');
        } else {
            this.classList.remove('iti-invalid-key');
        }
    });

    initOrder = function() {
        $scope.order = {
            expireDate: $filter('date')(new Date(), 'dd.MM.yyyy'),
            expireTime: $filter('date')(new Date(), 'H:MM'),
            reward: '',
            address: {coords: $rootScope.selfMarker.coords,
                addressField: orderAddress.getAddress()},
            namespace: 'FriendsOfFriends',
            description:'',
            items: [{}]
        }
    };

    initOrder();

    $scope.$on('updateOrderAddress', function() {
        $scope.order.address.coords = $rootScope.selfMarker.coords;
        var myposition = new google.maps.LatLng($rootScope.selfMarker.coords.latitude, $rootScope.selfMarker.coords.longitude);
        geocoder.geocode({'latLng': myposition},function(data,status) {
            if(status == google.maps.GeocoderStatus.OK) {
                orderAddress.setAddress(
                    data[0].formatted_address
                );
                $scope.$apply();
            }
        });
    });

    $scope.alerts = {warning: [], error: []};

    $scope.order = {
        expireDate: $filter('date')(new Date(), 'dd.MM.yyyy'),
        expireTime: $filter('date')(new Date(), 'H:MM'),
        reward: '',
        address: myAddress.getAddress(),
        namespace: 'FriendsOfFriends',
        description:'',
        items: [{}]
    };

    $scope.alerts = {warning: [], error: []};

    $scope.showAlert = function(message, type) {
        var alert = {message: message};
        $scope.alerts[type] = [alert];
    };

    $scope.hideAlert = function() {
        $scope.alerts.warning = [];
        $scope.alerts.error = [];
    };

    $scope.chooseTime = function() {
        var input = angular.element('#expire_in_time').clockpicker();
        input.clockpicker('show');
    };

    $scope.addToList = function() {
        $scope.order.items.push({});
    };

    $scope.itemInList = function(index) {
        return (index != $scope.order.items.length - 1);
    };

    $scope.removeFromList = function(index) {
        $scope.order.items.splice(index, 1);
    };

    $scope.createOrder = function() {
        var last_item_index = $scope.order.items.length - 1;
        var $last_item = $scope.order.items[last_item_index];
        if(!$last_item.comment) {
            $scope.order.items.splice(last_item_index, 1);
        }

        var newOrder = {
            items: $scope.order.items,
            expireIn: parseDate($scope.order.expireDate, $scope.order.expireTime).getTime(),
            latitude: myAddress.getAddress().latitude,
            longitude: myAddress.getAddress().longitude,
            reward: $scope.order.reward,
            telephoneNumber: $scope.telephoneInput[0].value.length > 0 ?
                $scope.telephoneInput.intlTelInput("getNumber") : null,
            status: 'New',
            owner: null,
            executor: null,
            address: $scope.order.address.plainAddress,
            doneAt: null,
            id: null,
            createdAt: null,
            namespace: $scope.order.namespace,
            description: $scope.order.description
        };

        var MAX_TEXT_SIZE = 200;
        var restrictions = [
            {
                isAppearing: newOrder.description.length > MAX_TEXT_SIZE,
                message: "Слишком длинное описание заказа"
            },
            {
                isAppearing: newOrder.reward.length > MAX_TEXT_SIZE,
                message: "Слишком длинное описание вознаграждения"
            },
            {
                isAppearing: newOrder.address.length > MAX_TEXT_SIZE,
                message: "Слишком длинный адрес"
            },
            {
                isAppearing: !$scope.telephoneInput.intlTelInput("isValidNumber")
                && $scope.telephoneInput[0].value.length > 0,
                message: "Некорректный номер телефона"
            }
        ];

        for (var i in restrictions) {
            if (restrictions[i].isAppearing) {
                $scope.showAlert(restrictions[i].message, 'warning');
                return;
            }
        }

        newOrder = Order.create(newOrder,
            function(successResponse) {
                angular.element('#createOrderModal').modal('hide');
                $scope.hideAlert();
                $scope.order = {
                    expireDate: $filter('date')(new Date(), 'dd.MM.yyyy'),
                    expireTime: $filter('date')(new Date(), 'hh:mm'),
                    reward: '',
                    address: myAddress.getAddress(),
                    namespace: 'FriendsOfFriends',
                    description:'',
                    items: [{}]
                };
                $rootScope.$broadcast('createdNewOrderEvent', newOrder);
            },
            function(errorResponse) {
                $scope.showAlert("Неизвестная техническая ошибка: попробуйте позже", 'error');
            }
        );
    };
});
