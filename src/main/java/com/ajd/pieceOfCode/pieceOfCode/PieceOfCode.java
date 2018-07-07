package com.ajd.pieceOfCode.pieceOfCode;

import com.ajd.pieceOfCode.user.User;
import com.ajd.pieceOfCode.util.View;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.*;

@Entity
public class PieceOfCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private User user;
    @Basic(optional = false)
    private String title;
    @Basic(optional = false)
    @Lob
    @Column(length=10000)
    private String code;
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    private CodeLanguage codeLanguage;
    @Basic(optional = false)
    private ZonedDateTime publicationDate;

    @PersistenceConstructor
    public PieceOfCode() {
    }

    @JsonCreator
    public PieceOfCode(@JsonProperty("title") String title, @JsonProperty("code") String code,
                       @JsonProperty("codeLanguage") CodeLanguage codeLanguage) {
        this.title = title;
        this.code = code;
        this.codeLanguage = codeLanguage;
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonView(View.Summary.class)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonView(View.Summary.class)
    public CodeLanguage getCodeLanguage() {
        return codeLanguage;
    }

    public void setCodeLanguage(CodeLanguage codeLanguage) {
        this.codeLanguage = codeLanguage;
    }

    @JsonView(View.Summary.class)
    public ZonedDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(ZonedDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

}
