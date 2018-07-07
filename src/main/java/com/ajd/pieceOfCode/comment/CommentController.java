package com.ajd.pieceOfCode.comment;

import com.ajd.pieceOfCode.pieceOfCode.PieceOfCode;
import com.ajd.pieceOfCode.user.User;
import com.ajd.pieceOfCode.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.ajd.pieceOfCode.pieceOfCode.PieceOfCodeController.CODE_REQUEST_PREFIX;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Transactional
@Validated
public class CommentController {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @JsonView(View.Summary.class)
    @RequestMapping(method = POST, value = CODE_REQUEST_PREFIX + "/comments/")
    @PreAuthorize("isAuthenticated()")
    public Comment postComment(User user, PieceOfCode pieceOfCode, @RequestBody Comment comment) {
        comment.setUser(user);
        comment.setPieceOfCode(pieceOfCode);
        return commentRepository.save(comment);
    }

    @JsonView(View.Summary.class)
    @RequestMapping(method = GET, value = CODE_REQUEST_PREFIX + "/comments/")
    @PreAuthorize("isAuthenticated()")
    public Page<Comment> getComments(User user, PieceOfCode pieceOfCode, Pageable pageable) {
        return commentRepository.findAllByPieceOfCodeOrderByPublicationDateAsc(pieceOfCode, pageable);
    }
}
