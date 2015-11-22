/**
 * Created by melge on 04.11.2015.
 */

ogifyApp.factory('ClickedOrder', function(Order, $rootScope) {
    var clickedOrder = {
        set: function (order) {
            this.order = order;
            this.rate = false;
        },
        setWithRate: function (order) {
            this.order = order;
            this.feedback = Order.getOrderRate({orderId: order.id}).$promise.then(function(response) {
                $rootScope.$broadcast("ClickedOrderRateResolved", response);
            });
        },
        setWithSocialRelationship: function (order, relationship) {
            this.order = order;
            this.socialRelationship = relationship;
        },
        getOrderRate: function () {
            if (this.feedback == null || this.feedback.$resolved == false) {
                return 0;
            }

            return this.feedback.rate;
        },
        isOrderRated: function () {
            if(this.feedback == null) {
                return false;
            }

            if(this.feedback.$resolved == true && this.feedback.rate == null) {
                return false;
            }

            return true;
        }
    };

    clickedOrder.order = {
        description: "",
        reward: "",
        items: [],
        address: null,
        expireIn: null,
        owner: {photoUri: null, fullName: null, vkId: null},
        executor: {photoUri: null, fullName: null}
    };

    return clickedOrder;
});
