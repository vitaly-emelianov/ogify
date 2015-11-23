/**
 * Created by melge on 18.11.2015.
 */
ogifyApp.directive('starRating', function() {
    return {
        restrict: 'A',
        templateUrl: 'templates/directives/star-rating.html',
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
                scope.starsClass = 'star-rating-' + (scope.ratingValue);
                for ( var i = 0; i < scope.max; i++) {
                    scope.stars.push({
                        filled : i < scope.ratingValue
                    });
                }


            };

            scope.getUlClass = function () {
                var classString = 'rating';
                if(scope.readonly != true) {
                    classString += ' unrated';
                } else if (scope.readonly == true) {
                    classString += ' rated';
                }

                return classString;
            };

            scope.getStarClass = function(ind) {
                var ret = 'star';
                if (scope.readonly == true) {
                    ret += ' star-rating-' + (scope.ratingValue);
                    if(ind < scope.ratingValue) {
                        ret += ' filled';
                    } else {
                        ret += ' unfilled';
                    }

                    elem.addClass(ret);
                }

                return ret;
            };

            scope.onStarMouseOver = function(ind) {
                if (scope.readonly != true) {
                    scope.starsClass = 'star-rating-' + (ind + 1);
                }
            };

            scope.onStarMouseOut = function() {
                if (scope.readonly != true) {
                    scope.starsClass = 'star-rating-' + (scope.ratingValue);
                }
            };

            scope.toggle = function(index) {
                if (scope.readonly == undefined || scope.readonly === false) {
                    scope.onRatingSelected({
                        rating : index + 1
                    });
                }
            };

            scope.$watch('ratingValue',
                function(newVal, oldVal) {
                    if(newVal != null) {
                        scope.ratingValue = newVal;
                        updateStars();
                    }
                }
            );
        }
    };
});