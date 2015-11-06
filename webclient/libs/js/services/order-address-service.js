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