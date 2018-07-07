angular.module('pieceOfCode').factory('UserService', function ($rootScope, $http, $cookies, $resource, amMoment, lodash, $state) {

    var self = this;
    var User = $resource('/user');

    self.user = User.get();

    var logout = function () {
        var cookies = $cookies.getAll();
        angular.forEach(cookies, function (v, k) {
            if (k === "JSESSIONID") {
                $cookies.remove(k);
            }
        });
        self.user = User.get();
        $state.go("login");
    };

    self.updateUser = function (user) {
        return $http.put('/user', user).then(function () {
            lodash.merge(self.user, user);
        });
    };

    self.getUser = function () {
        return self.user;
    };

    self.refreshAuthenticatedUser = function () {
        if (self.user.$resolved) {
            self.user = User.get();
        }
        return self.user.$promise;
    };

    self.logout = function () {
        return $http.get('/logout').finally(logout);
    };

    return self;
});