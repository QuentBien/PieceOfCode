package com.ajd.pieceOfCode.pieceOfCode;

import com.ajd.pieceOfCode.comment.ReactionEmote;
import com.ajd.pieceOfCode.comment.ReactionRepository;
import com.ajd.pieceOfCode.user.User;
import com.ajd.pieceOfCode.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


import java.util.HashMap;
import java.util.Map;

import static com.ajd.pieceOfCode.util.PieceOfCodeArgumentResolver.PIECE_OF_CODE_ID;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Transactional
@Validated
public class PieceOfCodeController {

    private final static String CODES_REQUEST_PREFIX = "/pieces-of-code/";
    public final static String CODE_REQUEST_PREFIX = CODES_REQUEST_PREFIX + "{" + PIECE_OF_CODE_ID + "}";
    private final PieceOfCodeRepository pieceOfCodeRepository;
    private final ReactionRepository reactionRepository;

    @Autowired
    public PieceOfCodeController(PieceOfCodeRepository pieceOfCodeRepository, ReactionRepository reactionRepository) {
        this.pieceOfCodeRepository = pieceOfCodeRepository;
        this.reactionRepository = reactionRepository;
    }

    @Transactional(readOnly = true)
    @JsonView(View.Summary.class)
    @RequestMapping(method = GET, value = CODES_REQUEST_PREFIX)
    @PreAuthorize("isAuthenticated()")
    public Page<PieceOfCodeDecorator> getPieceOfCodes(User user, Pageable pageable,
                                                      @RequestParam(value = "codeLanguage", required = false) CodeLanguage codeLanguage,
                                                      @RequestParam(value = "search", required = false) String search) {
        Page<PieceOfCode> pieceOfCodes;
        if (search != null) {
            pieceOfCodes = pieceOfCodeRepository.findAllBySearchOrderByPublicationDateDesc(search,
                    codeLanguage != null, codeLanguage, pageable);
        } else {
            pieceOfCodes = pieceOfCodeRepository.findAllByOrderByPublicationDateDesc(pageable);
        }
        return pieceOfCodes.map(pieceOfCode -> buildPieceOfCodeDecorator(pieceOfCode, user));
    }

    @Transactional(readOnly = true)
    @JsonView(View.Summary.class)
    @RequestMapping(method = GET, value = CODE_REQUEST_PREFIX)
    @PreAuthorize("isAuthenticated()")
    public PieceOfCodeDecorator getPieceOfCode(PieceOfCode pieceOfCode, User user) {
        return buildPieceOfCodeDecorator(pieceOfCode, user);
    }

    private PieceOfCodeDecorator buildPieceOfCodeDecorator(PieceOfCode pieceOfCode, User user) {
        Map<ReactionEmote, Long> reactionsNumber = new HashMap<>();
        reactionsNumber.put(ReactionEmote.LOVE, reactionRepository.countAllByPieceOfCodeAndReactionEmote(pieceOfCode, ReactionEmote.LOVE));
        reactionsNumber.put(ReactionEmote.UP, reactionRepository.countAllByPieceOfCodeAndReactionEmote(pieceOfCode, ReactionEmote.UP));
        reactionsNumber.put(ReactionEmote.HAHA, reactionRepository.countAllByPieceOfCodeAndReactionEmote(pieceOfCode, ReactionEmote.HAHA));
        reactionsNumber.put(ReactionEmote.DOWN, reactionRepository.countAllByPieceOfCodeAndReactionEmote(pieceOfCode, ReactionEmote.DOWN));
        reactionsNumber.put(ReactionEmote.ANGRY, reactionRepository.countAllByPieceOfCodeAndReactionEmote(pieceOfCode, ReactionEmote.ANGRY));
        return new PieceOfCodeDecorator(pieceOfCode, reactionRepository.findByPieceOfCodeAndUser(pieceOfCode, user), reactionsNumber);
    }

    @JsonView(View.Summary.class)
    @RequestMapping(method = POST, value = CODES_REQUEST_PREFIX)
    @PreAuthorize("isAuthenticated()")
    public JsonNode postPieceOfCode(User user, @RequestBody @Valid PieceOfCode pieceOfCode) {
        pieceOfCode.setUser(user);
        pieceOfCode = pieceOfCodeRepository.save(pieceOfCode);
        return JsonNodeFactory.instance.objectNode().put("id", pieceOfCode.getId());
    }

}
