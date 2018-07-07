angular.module('pieceOfCode').controller('PostController', function ($http, $state, ContentType) {
    var self = this;
    self.pieceOfCode = {};
    self.pieceOfCode.codeLanguage = "JAVA";
    self.refresh = true;
    self.editorOptions = {
        lineWrapping : true,
        lineNumbers: true,
        mode: ContentType[self.pieceOfCode.codeLanguage]
    };

    self.refreshCodeMirror = function () {
        self.editorOptions.mode = ContentType[self.pieceOfCode.codeLanguage];
    };

    self.post = function () {
        $http.post("/pieces-of-code/", self.pieceOfCode).then(function (result) {
            $state.go("pieceOfCode", {pieceOfCodeId: result.data.id});
        })
    };
});