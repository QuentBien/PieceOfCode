package com.ajd.pieceOfCode.comment;

import com.ajd.pieceOfCode.pieceOfCode.PieceOfCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    Page<Comment> findAllByPieceOfCodeOrderByPublicationDateAsc(PieceOfCode pieceOfCode, Pageable pageable);
}