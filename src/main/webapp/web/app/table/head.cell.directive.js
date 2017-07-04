/*let simpleTable = require('./table.module');*/
var module = angular.module('simpleTable');
module.directive('headCellDirective', function () {
   var self = this;
  return {
      scope: {
          item: '=headCellDirective'
      },
      link: function(scope, element, attrs) {
          scope.getContentUrl = function() {
              return scope.item.template;
          };

      },
      template: '<div ng-include="getContentUrl()"></div>'
  }
});


module.directive('filterDropdownDirective', function () {
    return {
        scope: {
            item: '=filterDropdownDirective'
        },
        link: function(scope, element, attrs) {
            scope.getContentUrl = function () {
                 return scope.item.columnFilter.template;
            };
            scope.applyFilter = function () {
                scope.item.columnFilter.apply();
                scope.$emit('applyFilter', scope.item);
                console.debug("applyFilter event is emit");
            };
            scope.clearFilter = function (name) {
                scope.item.columnFilter.remove(name);
                scope.$emit('applyFilter', scope.item);
                console.debug("applyFilter event is emit");
            }

        },
        template:  '<div ng-include="getContentUrl()"> </div>'
    }
});

module.directive('filterDateTime', function () {
    return {
        scope: {
            item: '=filterDateTime'
        },
        link: function (scope, element, attrs) {
            $(element).datetimepicker({
                format: "dd MM yyyy - hh:ii",
                autoclose: true,
                todayBtn: true,
                language: 'ru',
                weekStart: 1
            });
            $('.dateClear').click(function(e) {
                e.stopPropagation();
            });
            $(element).datetimepicker().on('changeDate', function (){
                var filterValue = attrs.filterValue;
                scope.item.columnFilter.toggleValue(filterValue, element.data("datetimepicker").getDate());
            });
            if(attrs.dateStartFrom){
                $(element).datetimepicker().on('show', function () {
                    $(element).datetimepicker('setStartDate', scope.item.columnFilter.from);
                })
            }

        }
    }
});

module.directive('customInfiniteScroll', function () {
    return {
        scope: {
            table: '=customInfiniteScroll'
        },
        link: function (scope, element, attrs) {
            $(element).on('scroll', function () {
                if($(this).scrollTop() + $(this).innerHeight()>=$(this)[0].scrollHeight)
                {
                    scope.table.loadPage();
                }
            })
        }
    }
});