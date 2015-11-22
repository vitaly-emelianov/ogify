/**
 * Created by melge on 18.11.2015.
 */
ogifyApp.directive('starRating', function() {
    return {
        restrict: 'A',
        templateUrl: '/templates/directives/star-rating.html',
        scope : {
            ratingValue : '=',
            max : '=?',
            onRatingSelected : '&?',
            readonly: '=?'
        },
        link : function(scope, elem, attrs) {
            if (scope.max == undefined) {
                scope.max = 5;
            }
            var updateStars = function() {
                scope.stars = [];
                for ( var i = 0; i < scope.max; i++) {
                    scope.stars.push({
                        filled : i < scope.ratingValue
                    });
                }
            };

            scope.getStarClass = function(ind) {
                var ret = 'star';
                if (scope.readonly == true) {
                    ret += ' star-rating-' + (scope.ratingValue);
                    if(ind < scope.ratingValue) {
                        ret += ' filled';
                    }

                    elem.addClass(ret);
                }

                return ret;
            };

            scope.setRaitsClass = function(ind) {
                if (scope.readonly != true) {
                    elem.addClass('stars-' + (ind + 1));
                }
            };

            scope.removeRaitsClass = function(ind) {
                if (scope.readonly != true) {
                    elem.removeClass('stars-' + (ind + 1));
                }
            };

            scope.toggle = function(index) {
                if (scope.readonly == undefined || scope.readonly === false) {
                    scope.ratingValue = index + 1;
                    scope.onRatingSelected({
                        rating : index + 1
                    });
                }
            };

            scope.$watch('ratingValue',
                function(newVal, oldVal) {
                    if(newVal != null) {
                        updateStars();
                    }
                }
            );
        }
    };
});