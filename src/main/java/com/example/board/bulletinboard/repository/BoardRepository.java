package com.example.board.bulletinboard.repository;

import com.example.board.bulletinboard.domain.Board;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

}
