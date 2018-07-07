angular.module('pieceOfCode').controller('LoginController', function ($rootScope, UserService, $location, credentials, $stateParams, $http, $httpParamSerializerJQLike) {
    var self = this;
    self.credentials = credentials;
    self.submitting = false;
    self.userToRegister = {};

    self.login = function () {
        self.submitting = true;
        $http.post('/login', $httpParamSerializerJQLike(self.credentials), {
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).then(UserService.refreshAuthenticatedUser).then(function () {
            $location.url(decodeURIComponent($stateParams.redirect || "/"));
        }).catch(function (error) {
            self.loginError = error;
        }).finally(function () {
            self.submitting = false;
        });
    };

    self.register = function () {
        self.submitting = true;
        $http.post('/users/', self.userToRegister).then(function () {
            self.credentials.email = self.userToRegister.email;
            self.credentials.password = self.userToRegister.password;
            self.login();
        }).catch(function (error) {
            self.registerError = error;
            self.submitting = false;
        });
    };

});