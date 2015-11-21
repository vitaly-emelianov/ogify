/**
 * Created by melge on 18.11.2015.
 */
ogifyApp.directive('starRating', function() {
    return {
        restrict : 'A',
        template : '<ul class="rating">'
        + '    <li ng-repeat="star in stars" ng-class="star" ng-mouseover="setRaitsClass($index)" ng-mouseout="removeRaitsClass($index)" ng-click="toggle($index)">'
        + '        <i class="fa fa-star"></i>'
        + '    </li>'
        + '</ul>',
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
                if (scope.readonly != undefined || scope.readonly !== false)
                    ret += ' star-' + (ind+1);
                return ret;
            };

            scope.setRaitsClass = function(ind) {
                if (scope.readonly != undefined || scope.readonly !== false)
                    elem.addClass('stars-' + (ind + 1));
            };

            scope.removeRaitsClass = function(ind) {
                if (scope.readonly != undefined || scope.readonly !== false)
                    elem.removeClass('stars-' + (ind + 1));
            }

            scope.toggle = function(index) {
                if (scope.readonly == undefined || scope.readonly === false) {
                    scope.ratingValue = index + 1;
                    scope.onRatingSelected({
                        rating : index + 1
                    });
                }
            };

            scope.$watch('ratingValue',
                function(oldVal, newVal) {
                    if (newVal || newVal == 0) {
                        updateStars();
                    }
                }
            );
        }
    };
});