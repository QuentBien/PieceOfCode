package com.ajd.pieceOfCode.comment;

import com.ajd.pieceOfCode.pieceOfCode.PieceOfCode;
import com.ajd.pieceOfCode.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReactionRepository extends CrudRepository<Reaction, Long> {
    Reaction findByPieceOfCodeAndUser(PieceOfCode pieceOfCode, User user);
    Optional<Reaction> findFirstByPieceOfCodeAndUser(PieceOfCode pieceOfCode, User user);
    Long countAllByPieceOfCodeAndReactionEmote(PieceOfCode pieceOfCode, ReactionEmote reactionEmote);

}
