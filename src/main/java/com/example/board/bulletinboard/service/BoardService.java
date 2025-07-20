package com.example.board.bulletinboard.service;

import com.example.board.bulletinboard.domain.Board;
import com.example.board.bulletinboard.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> findAllBoards(){
        return boardRepository.findAll();
    }

    public Optional<Board> findBoardById(long boardId){
        return boardRepository.findById(boardId);
    }

    @Transactional
    public Board createBoard(String title, String content, String writer, String password) {
        Board newBoard = Board.builder()
                .title(title)
                .content(content)
                .password(password)
                .writer(writer)
                .build();
        return boardRepository.save(newBoard);
    }

    @Transactional
    public Optional<Board> updateBoard(long id, String title, String content, String password) {
        return boardRepository.findById(id)
                .map(board -> {
                   if(!board.verifyPassword(password)) {
                       throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
                   }
                   board.update(title, content);
                   return board;
                });
    }

    @Transactional
    public void deleteBoard(long id, String password) {
        boardRepository.findById(id).ifPresentOrElse(board -> {
            if(!board.verifyPassword(password)) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            boardRepository.delete(board);
        }, () -> {
            throw new IllegalArgumentException("해당 게시글이 없습니다.");
        });
    }
}
