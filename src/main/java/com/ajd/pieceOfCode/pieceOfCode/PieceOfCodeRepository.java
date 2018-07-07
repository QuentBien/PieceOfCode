package com.ajd.pieceOfCode.pieceOfCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface PieceOfCodeRepository extends CrudRepository<PieceOfCode, Long> {
    Page<PieceOfCode> findAllByOrderByPublicationDateDesc(Pageable pageable);
    @Query(value ="SELECT p FROM PieceOfCode p " +
        "WHERE p.user.firstName LIKE ?1 " +
        "OR p.user.lastName LIKE ?1 " +
        "OR p.title LIKE ?1 " +
        "OR p.code LIKE ?1 " +
        "AND (FALSE = ?2 OR p.codeLanguage = ?3) " +
        "ORDER BY p.publicationDate DESC ")
    Page<PieceOfCode> findAllBySearchOrderByPublicationDateDesc(String search, boolean isCodeLanguage,
                                                                CodeLanguage codeLanguage, Pageable pageable);
}
