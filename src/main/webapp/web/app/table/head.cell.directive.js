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
                switch (scope.item.dataType) {
                    case DataType.STRING:
                        return '/table/headTemplate/textFilter.html';
                        break;
                    case DataType.ENUM:
                        return '/table/headTemplate/enumFilter.html';
                        break;
                    case DataType.DATE:
                        return '/table/headTemplate/dateFilter.html';
                        break;
                }
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

            $(element).datetimepicker().on('changeDate', function (){
                if(attrs.dateto){
                    scope.item.columnFilter._to = element.data("datetimepicker").getDate();
                } else {
                    scope.item.columnFilter._from = element.data("datetimepicker").getDate();
                }
                scope.$emit('dateFilterChanged', scope.item);
                scope.item.columnFilter.applyFilter();
            });
            if(attrs.dateto){
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