/*import './bower_components/jquery/dist/jquery';*/

var angular = require('./bower_components/angular');
var mailList = require('./mail-list/mail.list.module');

/*import './bower_components/smalot-bootstrap-datetimepicker/js/bootstrap-datetimepicker';*/


/*export default*/ angular.module('mailApp', ['mailList']).config(function ($compileProvider) {
    var debugEnabled = $compileProvider.debugInfoEnabled();
    console.log("debugInfoEnabled=", debugEnabled);
});