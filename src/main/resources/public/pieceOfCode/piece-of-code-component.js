angular.module('pieceOfCode').component('pieceOfCode', {
    templateUrl: 'pieceOfCode/piece-of-code-component.html',
    controllerAS: '$ctrl',
    bindings: {
        'pieceOfCode': '<'
    },
    controller: function (ContentType) {
        var self = this;
        self.editorOptions = {
            lineWrapping: true,
            lineNumbers: true,
            readOnly: 'nocursor',
            mode: ContentType[self.pieceOfCode.codeLanguage]
        };
    }
});