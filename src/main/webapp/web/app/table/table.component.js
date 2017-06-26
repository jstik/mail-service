/*let simpleTable = require('./table.module');*/
var module = angular.module('simpleTable');
function HeadCell(parameters) {
    this.label = parameters.data;
    this.template = parameters.template;
    this.name = parameters.name || '';
    this.selected = parameters.selected || false;
    this.dataType = parameters.dataType;
    this.filter = parameters.filter || false;
    this.columnFilter = new ColumnFilter();
}
function ColumnFilter() {
    this.filterValue = '';
    this.from = null;
    this.to = null;
    this._from = null;
    this._to = null;
    this.applied = 0;
    this.applyFilter = function () {
        this.applied++;
    };
    this.clear = function () {
        this.applied = 0;
        this.filterValue = '';
        this.from = null;
        this.to = null;
        this._from = null;
        this._to = null;
    };

    this.isApplied = function () {
        return this.applied > 0;
    };
}

const DataType = {
    STRING: 0,
    DATE: 1,
    ENUM: 2
};

function RowCell(parameters) {
    this.id = parameters.id;
    this.data = parameters.data;
    this.template = parameters.template;
    this.name = parameters.name;
    this.class = parameters.class;
    this.func = parameters.func;
    this.selected = parameters.selected || false;
    this.confirmTitle = parameters.confirmTitle;
}

function Row(id) {
    this.id = id;
    this.cells = [];
    this.pushCell = function (rowCell) {
        this.cells.push(rowCell)
    };
    this.unshiftCell = function (rowCell) {
        this.cells.unshift(rowCell)
    }
}

function Table(options) {
    var self = this;
    this.head = []; //HeadCell
    this.body = []; //Rows
    this.currentPage = 0;
    this.loaded = false;
    this.options = options;

    this.loadPage = function () {
        var filter = JSON.stringify(self.getColumnFilter());
        return self.options.$http.get(self.options.dataUrl,
            {
                params: {filter: filter},
                headers: {
                    'Content-Type': 'application/json;charset=utf-8;'
                }
            }
       ).then(function (response) {
            self.loaded = true;
            self.currentPage = self.currentPage + 1;
            return response;
        }).then(function (response) {
           options.dataLoadCallback.func(response.data.data, options.dataLoadCallback.params);
           return response;
       });
    };

    this.getColumnFilter = function () {
        var filter = [];
        for (var i = 0; i < this.head.length; i++) {
            var headCell = this.head[i];
            if (headCell.filter && headCell.columnFilter.applied) {
                filter.push({
                    name: headCell.name,
                    value: {
                        filterValue: headCell.columnFilter.filterValue,
                        from: headCell.columnFilter._from,
                        to: headCell.columnFilter._to
                    }
                });
            }
        }
        return filter;
    };
    this.resetTable = function () {
        self.currentPage = 0;
        self.body = [];
        self.loadPage();
    };
    this.getColumn = function (index) {
       var column = [];
       for(var i = 0; i < self.body.length; i++ ){
           column.push(self.body[i].cells[index]);
       }
       return column;
    };

    this.getColumnCount = function () {
       return self.body.length;
    }

}


module.component('simpleTable', {
    css: 'table/css/table.css',
    templateUrl: 'table/table.template.html',
    controller: ['$scope', '$timeout', function simpleTableController($scope, $timeout) {
        var self = this;
        this.table = $scope.$parent.table;
        $scope.$on('dateFilterChanged',  function (event, data) {
            console.log(data);
            self.table.resetTable();
        });
        $scope.$watch(function ($scope) {
            return self.table.head.map(function (headCell) {
                if (headCell.filter && headCell.dataType !== DataType.DATE) {
                    return headCell.columnFilter.applied;
                } else {
                    return false;
                }

            });
        }, function (newValue, oldValue) {

                $timeout(self.table.resetTable, 500);

        }, true);
    }],
    controllerAs: 'tableController'


});
angular.module('simpleTable').directive('actionWithConfirm', function () {
        return {
            scope: {
                item: '=actionWithConfirm'
            },
            link: function (scope, element, attrs) {
                scope.$evalAsync(function () {
                    // Finally, directives are evaluated
                    // and templates are renderer here
                    $('[data-toggle=confirmation]').confirmation({
                        rootSelector: '[data-toggle=confirmation]',
                        container: 'body',
                        title: scope.item.confirmTitle,
                        onConfirm: function () {
                            scope.item.func.action(scope.item.id)
                        }
                    });
                })
            }
        }
    }
);


