package com.ajd.pieceOfCode.comment;

import com.ajd.pieceOfCode.pieceOfCode.PieceOfCode;
import com.ajd.pieceOfCode.user.User;
import com.ajd.pieceOfCode.util.View;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @Basic(optional = false)
    private String text;
    @Basic(optional = false)
    private ZonedDateTime publicationDate;
    @ManyToOne
    private PieceOfCode pieceOfCode;

    @PersistenceConstructor
    public Comment() {
    }

    @JsonCreator
    public Comment(@JsonProperty("text") String text) {
        this.text = text;
        this.publicationDate = ZonedDateTime.now();
    }

    @JsonView(View.Summary.class)
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
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonView(View.Summary.class)
    public ZonedDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(ZonedDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public PieceOfCode getPieceOfCode() {
        return pieceOfCode;
    }

    public void setPieceOfCode(PieceOfCode pieceOfCode) {
        this.pieceOfCode = pieceOfCode;
    }
}
