/*var mailList = require('./mail.list.module');*/
var module = angular.module('mailList');

function createMailHead() {
    return [

        new HeadCell({
            data: 'from',
            template: '/table/headTemplate/simpleCellTemplate.html',
            name: 'from',
            dataType : DataType.STRING,
            filter: true
        }),
        new HeadCell({
            data: 'to',
            template: '/table/headTemplate/simpleCellTemplate.html',
            name: 'to',
            dataType : DataType.STRING,
            filter: true
        }),
        new HeadCell({
            data: 'status',
            template: '/table/headTemplate/simpleCellTemplate.html',
            name: 'status',
            dataType : DataType.ENUM,
            filter: true
        }),
        new HeadCell({
            data: 'created',
            template: '/table/headTemplate/simpleCellTemplate.html',
            name: 'created',
            dataType : DataType.DATE,
            filter: true
        }),
        new HeadCell({
            data: 'updated',
            template: '/table/headTemplate/simpleCellTemplate.html',
            name: 'updated',
            dataType : DataType.DATE,
            filter: true
        }),
        new HeadCell({
            data: 'type',
            template: '/table/headTemplate/simpleCellTemplate.html',
            name: 'type',
            dataType : DataType.ENUM,
            filter: true
        }),
        new HeadCell({
            data: '',
            template: '/table/headTemplate/simpleCellTemplate.html',
            name: 'action',
        }),

    ]
}
function createCheckboxCell(id, scope) {
    var cell = new RowCell({
        id: id,
        name: 'checkbox',
        data: '',
        template: '/table/cellTemplate/checkbox.html'
    });
    scope.$watch(function() { return scope.table.head[0]}, function (newValue, oldValue, scope) {
        cell.selected = newValue.selected;
    }, true);
    return cell;
}

function createTable(data, params) {
    function statusClass(status) {
        var s = status.toLowerCase();
        return 'status' + s.charAt(0).toUpperCase() + s.slice(1);
    }

    for (var i = 0, len = data.length; i < len; i++) {
        var mail = data[i];
        var row = new Row(mail.id);
        row.pushCell(createCheckboxCell(mail.id, params.$scope));
        row.pushCell(new RowCell({
            id: mail.id,
            name: 'from',
            data: mail.from,
            template: '/table/cellTemplate/defaultCell.html'
        }));
        row.pushCell(new RowCell({
            id: mail.id,
            name: 'to',
            data: mail.to,
            template: '/table/cellTemplate/defaultCell.html'
        }));
        row.pushCell(new RowCell({
            id: mail.id,
            name: 'status',
            data: mail.status,
            template: '/table/cellTemplate/statusCell.html',
            class: statusClass(mail.status),
        }));
        row.pushCell(new RowCell({
            id: mail.id,
            name: 'created',
            data: mail.created,
            template: '/table/cellTemplate/defaultCell.html'
        }));
        row.pushCell(new RowCell({
            id: mail.id,
            name: 'updated',
            data: mail.updated,
            template: '/table/cellTemplate/defaultCell.html'
        }));
        row.pushCell(new RowCell({
            id: mail.id,
            name: 'type',
            data: mail.type,
            template: '/table/cellTemplate/defaultCell.html'
        }));
        row.pushCell(new RowCell({
                id: mail.id, name: '',
                data: 're-send',
                template: '/table/cellTemplate/buttonWithConfirm.html',
                confirmTitle: 'Re-send?',
                func: {
                    action: function (id) {
                        console.log('resend mail ' + id);
                        params.$http.post('mails/resend', "id=" + 1, {
                            headers: {
                                'Content-Type': 'application/json;charset=utf-8;'
                            }
                        }).success(function (data, status, headers, config) {
                            scope.resendResult = data;
                        })
                    }
                }
            }
        ));
        params.$scope.table.body.push(row);
    }
}


module.component('mailListComponent', {
    css: 'mail-list/mail.list.css',
    templateUrl: 'mail-list/mail.list.template.html',
    controller: ['$http', '$scope',
        function MailListController($http, $scope) {
            var self = this;
            $scope.table = new Table({
                $http :  $http,
                dataUrl : 'mails/mails.json',
                dataLoadCallback : {
                    func : createTable,
                    params : {$scope : $scope, $http : $http}
                }
            });
            $scope.table.head = createMailHead();
            $scope.checkAll =  new HeadCell({
                data: '',
                template: '/table/headTemplate/checkbox.html',
                name: 'checkAll',
            });
            $scope.table.head.unshift($scope.checkAll);

           $scope.table.loadPage();

            $scope.deleteMails = function () {
                var del = [];
                var ids =[];
                var checkboxes = $scope.table.getColumn(0);
                for(var i = 0; i< checkboxes.length; i++){
                    if(checkboxes[i].selected){
                        del.push(
                            {index: i,
                                id : checkboxes[i].id
                            });
                        ids.push(checkboxes[i].id);
                    }

                }
                $http.post('/mails/delete', ids).success(function (data, status, headers, config) {
                    $scope.resendResult = data;
                });
                for(var i = 0; i< del.length; i++) {
                    delete $scope.table.body[del[i].index];
                }
                var spliced = 0;
                var rowsCount = $scope.table.body.length;
                for(var i = 0; i< rowsCount; i++) {
                    if( !$scope.table.body[i-spliced]) {
                        $scope.table.body.splice(i-spliced, 1) ;
                        spliced++;
                    }
                }
            }


        }]
});
