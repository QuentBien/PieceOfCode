angular.module('pieceOfCode').controller('PieceOfCodeController', function ($http, pieceOfCode, lodash) {
    var self = this;
    self.pieceOfCode = pieceOfCode;

    self.react = function ($reaction) {
        $http.post("/pieces-of-code/" + self.pieceOfCode.id + "/reactions/", {reactionEmote: $reaction}).then(function (result) {
            if (self.pieceOfCode.userReaction) {
                self.pieceOfCode.reactionsNumber[self.pieceOfCode.userReaction.reactionEmote] = self.pieceOfCode.reactionsNumber[self.pieceOfCode.userReaction.reactionEmote] - 1;
            } else {
                self.pieceOfCode.userReaction = {};
            }
            self.pieceOfCode.userReaction = result.data;
            self.pieceOfCode.reactionsNumber[self.pieceOfCode.userReaction.reactionEmote] = self.pieceOfCode.reactionsNumber[self.pieceOfCode.userReaction.reactionEmote] + 1;
        });
    };

    self.submittingComment = false;

    self.comments = [];
    self.commentsNumber = 0;

    self.commentPost = function () {
        self.submittingComment = true;
        $http.post("/pieces-of-code/" + self.pieceOfCode.id + "/comments/", {text: self.comment}).then(function () {
            self.comment = "";
            self.commentsNumber = self.commentsNumber + 1;
            self.getNextComments();
        }).finally(function () {
            self.submittingComment = false;
        });
    };

    self.params = {
        page: 0,
        size: 5
    };

    self.getNextComments = function () {
        if (self.comments.length > 0 && self.commentsNumber > self.comments.length && self.comments.length % 5 === 0) {
            self.params.page = self.params.page + 1;
        }
        $http.get("/pieces-of-code/" + self.pieceOfCode.id + "/comments/", {params: self.params}).then(function (result) {
            lodash.forEach(result.data.elements, function(newComment) {
                if(!lodash.some(self.comments, {id: newComment.id})) {
                    self.comments.push(newComment);
                }
            });
            self.commentsNumber = result.data.totalElements;
        });
    };

    self.getNextComments();

});