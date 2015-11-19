/**
 * Created by melge on 04.11.2015.
 */

ogifyApp.factory('ClickedOrder', function() {
    var clickedOrder = {
        set: function(order) {
            this.order = order;
            this.rate = false;
        },
        setWithRate: function(order, rate) {
            this.order = order;
            this.rate = rate;
        },
        setWithSocialRelationship: function(order, relationship) {
            this.order = order;
            this.socialRelationship = relationship;
        }
    };

    clickedOrder.order = {
        description: null,
        reward: null,
        items: [],
        address: null,
        expireIn: null,
        owner: {photoUri: null, fullName: null, vkId: null},
        executor: {photoUri: null, fullName: null}
    };

    return clickedOrder;
});
