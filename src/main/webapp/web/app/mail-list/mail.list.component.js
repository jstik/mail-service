/*var mailList = require('./mail.list.module');*/
var module = angular.module('mailList');
module.component('mailListComponent', {
    css: 'mail-list/mail.list.css',
    templateUrl: 'mail-list/mail.list.template.html',
    controller: ['$http', '$scope',
        function MailListController($http, $scope) {
            var self = this;
            $scope.table = new Table({
                $http: $http,
                dataUrl: 'mails/mails.json',
                schemaUrl: '/mails/schema/schema.json',
                cells: [{
                    name: 'checkAll',
                    label : '',
                    dataType: DataType.CUSTOM,
                    head: {
                        template: '/table/headTemplate/checkbox.html',
                    },
                    row: {
                        template: '/table/cellTemplate/checkbox.html',
                        onInit : function(cell){
                            $scope.$watch(function() { return $scope.table.head[0]}, function (newValue) {
                                cell.selected = newValue.selected;
                            }, true);
                        }
                    }
                }, {
                    name: 'from',
                    label : 'from',
                    filter: true,
                    row: {
                        data: 'from',
                    }
                }, {
                    name: 'to',
                    label : 'to',
                    filter: true,
                    row: {
                        template: '/table/cellTemplate/defaultCell.html',
                        data: 'to',
                    }
                }, {
                    name: 'status',
                    label : 'status',
                    filter: true,
                    row: {
                        data: 'status',
                        template: '/table/cellTemplate/statusCell.html',
                        class: function className(data) {
                            var s = data.toLowerCase();
                            return 'status' + s.charAt(0).toUpperCase() + s.slice(1);
                        }
                    }
                }, {

                    name: 'created',
                    label : 'created',
                    filter: true,
                    row: {
                        data: 'created'
                    }
                }, {
                    name: 'updated',
                    label : 'updated',
                    filter: true,
                    row: {
                        data: 'updated'
                    }
                }, {
                    name: 'type',
                    label : 'type',
                    filter: true,
                    row: {
                        data: 'type',
                    }
                }, {
                    name: 'action',
                    label : '',
                    row: {
                        template: '/table/cellTemplate/buttonWithConfirm.html',
                        label: 're-send',
                        confirmTitle: 'Re-send?',
                        func: {
                            action: function (id) {
                                console.log('resend mail ' + id);
                                $http.post('mails/resend', "id=" + 1, {
                                    headers: {
                                        'Content-Type': 'application/json;charset=utf-8;'
                                    }
                                }).success(function (data, status, headers, config) {
                                    scope.resendResult = data;
                                })
                            }
                        }
                    }
                }]
            });
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
