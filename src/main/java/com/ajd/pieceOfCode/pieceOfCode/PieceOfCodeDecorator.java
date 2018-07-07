package com.ajd.pieceOfCode.pieceOfCode;

import com.ajd.pieceOfCode.comment.Reaction;
import com.ajd.pieceOfCode.comment.ReactionEmote;
import com.ajd.pieceOfCode.util.View;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;

/**
 * Decorates a PieceOfCode with authenticated user Reaction in it, for front-end purpose
 */
public class PieceOfCodeDecorator {

    @JsonUnwrapped
    @JsonView(View.Summary.class)
    public final PieceOfCode pieceOfCode;
    @JsonView(View.Summary.class)
    public final Reaction userReaction;
    @JsonView(View.Summary.class)
    public final Map<ReactionEmote, Long> reactionsNumber;

    PieceOfCodeDecorator(PieceOfCode pieceOfCode, Reaction userReaction, Map<ReactionEmote, Long> reactionsNumber) {
        this.pieceOfCode = pieceOfCode;
        this.userReaction = userReaction;
        this.reactionsNumber = reactionsNumber;
    }
}
