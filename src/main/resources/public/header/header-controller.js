angular.module('pieceOfCode').controller('HeaderController', function ($rootScope, UserService) {
    var self = this;
    self.user = UserService.getUser();

    self.logout = function () {
        UserService.logout();
    }
});