 /*simpleTable = require('./table.module');*/
var module = angular.module('simpleTable');
module.directive('rowCellDirective', function () {
    return {
        scope: {
            item: '=rowCellDirective'
        },
        link: function(scope, element, attrs) {
            scope.getContentUrl = scope.item.template;

        },
        template: '<div ng-include="getContentUrl"></div>'
    }
});