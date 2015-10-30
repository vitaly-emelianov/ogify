/**
 * Created by melge on 12.07.2015.
 */

/**
 * Parse date in format dd.mm.yyyy and time hh:mm
 */
var ogifyApp = angular.module('ogifyApp', ['ogifyServices', 'ngRoute', 'ngCookies', 'uiGmapgoogle-maps', 'angulartics',
    'angulartics.google.analytics', 'ngSanitize']);

function parseDate(date, time) {
    dateItems = date.split('.');
    timeItems = time.split(':');

    return new Date(dateItems[2], dateItems[1] - 1, dateItems[0], timeItems[0], timeItems[1], 0, 0);
}

function isOrderOutdated(order) {
    var orderDate = new Date(order.expireIn);
    var currentDate = new Date();
    return orderDate < currentDate;
};