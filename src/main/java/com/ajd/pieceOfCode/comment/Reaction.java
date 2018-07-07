package com.ajd.pieceOfCode.comment;

import com.ajd.pieceOfCode.pieceOfCode.PieceOfCode;
import com.ajd.pieceOfCode.user.User;
import com.ajd.pieceOfCode.util.View;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;

@Entity
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private ReactionEmote reactionEmote;
    @ManyToOne
    private PieceOfCode pieceOfCode;

    @PersistenceConstructor
    public Reaction() {
    }

    @JsonCreator
    public Reaction(@JsonProperty("user") User user, @JsonProperty("reactionEmote") ReactionEmote reactionEmote,
                    @JsonProperty("pieceOfCode") PieceOfCode pieceOfCode) {
        this.user = user;
        this.reactionEmote = reactionEmote;
        this.pieceOfCode = pieceOfCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(View.Summary.class)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonView(View.Summary.class)
    public ReactionEmote getReactionEmote() {
        return reactionEmote;
    }

    public void setReactionEmote(ReactionEmote reactionEmote) {
        this.reactionEmote = reactionEmote;
    }

    public PieceOfCode getPieceOfCode() {
        return pieceOfCode;
    }

    public void setPieceOfCode(PieceOfCode pieceOfCode) {
        this.pieceOfCode = pieceOfCode;
    }
}
