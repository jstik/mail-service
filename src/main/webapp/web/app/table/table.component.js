/*let simpleTable = require('./table.module');*/
var module = angular.module('simpleTable');
function HeadCell(parameters) {
    this.label = parameters.label;
    this.template = parameters.template;
    this.name = parameters.name || '';
    this.selected = parameters.selected || false;
    this.dataType = parameters.dataType;
    this.columnFilter = null;
}
function ColumnFilter(template, values) {
    this.filterValue = '';
    this.value = {};
    this.view = {};
    this.template = template;
    this.values = values;
    this.applied = false;
    this.set = function (name, value) {
        this.value[name] = value;
    };
    this.remove = function (name) {
        delete  this.value[name];
        if (this.view[name])
            this.view[name] = null;
        if(!this.hasValue()){
            this.applied = false;
        }
    };
    this.toggleValue = function (name, value) {
        if (value) {
            this.set(name, value)
        } else {
            this.remove(name);
        }
    };
    this.apply = function () {
      this.applied = true;
    };
    this.hasValue = function () {
       return Object.keys(this.value).length !== 0
    }
}

const DataType = {
    STRING: {
        id: 0,
        filterTemplate: '/table/headTemplate/textFilter.html',
        headTemplate: '/table/headTemplate/simpleCellTemplate.html',
        dataCellTemplate: '/table/cellTemplate/defaultCell.html'
    },
    DATE: {
        id: 1,
        filterTemplate: '/table/headTemplate/dateFilter.html',
        headTemplate: '/table/headTemplate/simpleCellTemplate.html',
        dataCellTemplate: '/table/cellTemplate/defaultCell.html'
    },
    ENUM: {
        id: 2,
        filterTemplate: '/table/headTemplate/enumFilter.html',
        headTemplate: '/table/headTemplate/simpleCellTemplate.html',
        dataCellTemplate: '/table/cellTemplate/defaultCell.html'
    },
    BOOLEAN: {
        id: 3,
        filterTemplate: '/table/headTemplate/booleanFilter.html',
        headTemplate: '/table/headTemplate/simpleCellTemplate.html',
        dataCellTemplate: '/table/cellTemplate/defaultCell.html'
    },
    CUSTOM: {
        id: 4,
        filterTemplate: '',
        headTemplate: '/table/headTemplate/simpleCellTemplate.html',
        dataCellTemplate: ''
    },
    NUMBER: {
        id: 5,
        filterTemplate: '/table/headTemplate/textFilter.html',
        headTemplate: '/table/headTemplate/simpleCellTemplate.html',
        dataCellTemplate: '/table/cellTemplate/defaultCell.html'
    }
};

function createHeadCell(settings) {
    let headCell = new HeadCell({
        template: settings.headTemplate(),
        name: settings.name,
        label:settings.label,
    });
    if (settings.filter) {
        headCell.columnFilter = new ColumnFilter(settings.filterTemplate(), settings.enum);
        if(settings.dataType === DataType.STRING){
            headCell.columnFilter.view = {text : null} ;
        } else if(settings.dataType === DataType.DATE){
            headCell.columnFilter.view = {to : null, from : null} ;
        } else if(settings.dataType === DataType.ENUM){
           for(let i = 0; i < settings.enum.length; i++){
               headCell.columnFilter.view[settings.enum[i]] = null;
           }
        }

    }
    return headCell
}

function createSettings(cell, property) {
    let settings = {};

    function dataType(property) {
        if (!property)
            return DataType.CUSTOM;
        let type = property.type;
        let format = property.format;
        if (type === "string" && format && format.indexOf("date") !== -1) {
            return DataType.DATE;
        } else if (property.enum) {
            return DataType.ENUM;
        } else if (type === "string") {
            return DataType.STRING;
        } else if (type === "integer") {
            return DataType.NUMBER;
        }
        return DataType.CUSTOM;

    }

    settings.dataType = cell.dataType || dataType(property);
    settings.cell = cell;
    settings.name = cell.name;
    settings.headTemplate = function () {
        if (!cell.head) {
            return this.dataType.headTemplate;
        }
        return cell.head.template || this.dataType.headTemplate;
    };
    settings.filterTemplate = function () {
        return this.dataType.filterTemplate;
    };
    settings.dataTemplate = function () {
        if (!cell.row) {
            return this.dataType.dataCellTemplate;
        }
        return cell.row.template || this.dataType.dataCellTemplate;
    };
    if (property) {
        settings.enum = property.enum;
        settings.pattern = property.pattern;
    }
    settings.name = cell.name;
    settings.label = cell.label ;
    settings.propertyName = cell.row ? cell.row.data || '' : '';
    settings.filter = cell.filter;
    settings.dataCell = cell.row || {};
    return settings;
}

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
        this.cells.push(rowCell);
    };
}

function Table(options) {
    var self = this;
    this.head = []; //HeadCell
    this.body = []; //Rows
    this.currentPage = 0;
    this.loaded = false;
    this.options = options;
    this.settings = [];
    this.lastPage = false;

    this.loadSettings = function (schema) {
        for (let i = 0; i < self.options.cells.length; i++) {
            let propertyName = self.options.cells[i].row.data;
            self.settings.push(createSettings(self.options.cells[i], schema[propertyName]));
        }
    };
    this.createHead = function () {
        for (let i = 0; i < self.settings.length; i++) {
            this.head.push(createHeadCell(self.settings[i]));
        }
    };

    this.loadPage = function () {
        if(self.lastPage)
            return;

        let filter = JSON.stringify(self.getColumnFilter());
        return self.options.$http.get(self.options.dataUrl,
            {
                params: {
                    filter: filter,
                    page: this.currentPage
                },
                headers: {
                    'Content-Type': 'application/json;charset=utf-8;'
                }
            }
        ).then(function (response) {
            self.loaded = true;
            self.currentPage = self.currentPage + 1;
            if(response.data.data.length === 0){
                self.lastPage = true;
            }
            for (let i = 0; i < response.data.data.length; i++) {
                let rowData = response.data.data[i];
                let row = new Row(rowData.id);
                for (let j = 0; j < self.settings.length; j++) {
                    let cellData = rowData[self.settings[j].propertyName];
                    let cell = new RowCell({
                        id: rowData.id,
                        data: cellData || self.settings[j].dataCell.label,
                        template: self.settings[j].dataTemplate(),
                        name: self.settings[j].name,
                        func: self.settings[j].dataCell.func,
                        confirmTitle: self.settings[j].dataCell.confirmTitle
                    });
                    if (self.settings[j].dataCell.onInit) {
                        self.settings[j].dataCell.onInit(cell);
                    }
                    if (self.settings[j].dataCell.class) {
                        cell.class = self.settings[j].dataCell.class(cell.data);
                    }
                    row.pushCell(cell);
                }
                self.body.push(row);
            }
            self.loaded = true;
            return response;
        })
    };

    this.getColumnFilter = function () {
        let filter = [];
        for (let i = 0; i < this.head.length; i++) {
            let headCell = this.head[i];
            let columnFilter = headCell.columnFilter;
            if (columnFilter && columnFilter.applied && columnFilter.hasValue()) {
                filter.push({
                    name: headCell.name,
                    value: columnFilter.value
                });
            }
        }
        return filter;
    };
    this.resetTable = function () {
        self.currentPage = 0;
        self.body = [];
        this.lastPage = false;
        self.loadPage();
    };
    this.getColumn = function (index) {
        let column = [];
        for (let i = 0; i < self.body.length; i++) {
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
    controller: ['$scope', '$timeout', '$http', '$q', function simpleTableController($scope, $timeout, $http, $q) {
        var self = this;
        this.table = $scope.$parent.table;
        $http.get(this.table.options.schemaUrl).then(function (response) {
            self.table.loadSettings(response.data.properties);
            self.table.createHead();
        }).then(function () {
            $q(function(resolve, reject) {
                self.table.loadPage()
            }) ;
        });

        $scope.$on('applyFilter', function (event, data) {
            console.log(data);
            self.table.resetTable();
        });
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


