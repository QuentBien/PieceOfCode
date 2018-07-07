package com.ajd.pieceOfCode.comment;

import com.ajd.pieceOfCode.pieceOfCode.PieceOfCode;
import com.ajd.pieceOfCode.user.User;
import com.ajd.pieceOfCode.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.ajd.pieceOfCode.pieceOfCode.PieceOfCodeController.CODE_REQUEST_PREFIX;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Transactional
@Validated
public class ReactionController {

    private final ReactionRepository reactionRepository;

    @Autowired
    public ReactionController(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    @JsonView(View.Summary.class)
    @RequestMapping(method = POST, value = CODE_REQUEST_PREFIX + "/reactions/")
    @PreAuthorize("isAuthenticated()")
    public Reaction react(User user, PieceOfCode pieceOfCode, @RequestBody Reaction reaction) {
        reactionRepository.findFirstByPieceOfCodeAndUser(pieceOfCode, user).ifPresent(reactionRepository::delete);
        reaction.setUser(user);
        reaction.setPieceOfCode(pieceOfCode);
        return reactionRepository.save(reaction);
    }
}
