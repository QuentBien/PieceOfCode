angular.module('pieceOfCode').controller('HomeController', function ($http) {
    var self = this;

    self.piecesOfCode = [];
    self.piecesOfCodeNumber = 0;

    self.params = {
        page: 0,
        size: 6
    };

    self.getNextPiecesOfCode = function () {
        $http.get("/pieces-of-code/", {params: self.params}).then(function (result) {
            self.piecesOfCodeNumber = result.data.totalElements;
            self.piecesOfCode =self.piecesOfCode.concat(result.data.elements);
            self.params.page = self.params.page + 1;
        });
    };

    self.getNextPiecesOfCode();

});