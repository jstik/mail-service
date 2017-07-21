/*import './bower_components/jquery/dist/jquery';*/

var angular = require('./bower_components/angular');
var mailList = require('./mail-list/mail.list.module');
var ngCookies = require('./bower_components/angular-cookies');
import './bower_components/bootstrap/dist/css/bootstrap.css';

var module =angular.module('mailApp', [ 'mailList', 'ngCookies']);

module.service('OAuth', function(){
	var self = this;
	this.setAccessToken = function (token) {
        self.accessToken = token;
    };
    this.getAccessToken = function () {
		return self.accessToken;
    }
});

module.factory('oauthHttpInterceptor', function (OAuth, $cookies) {
    return {
        request: function (config) {
            var token = $cookies.get('Authorization');
            if(token){
                OAuth.setAccessToken(token) ;
            }
            if (OAuth.getAccessToken()) {
                config.headers.Authorization = 'Bearer ' + OAuth.getAccessToken();
            }
            return config;
        }
    };
});
module.config(function ($httpProvider) {
    $httpProvider.interceptors.push('oauthHttpInterceptor');
});

	/*export default*/ module.config(function($compileProvider) {
	var debugEnabled = $compileProvider.debugInfoEnabled();
	console.log("debugInfoEnabled=", debugEnabled);
});