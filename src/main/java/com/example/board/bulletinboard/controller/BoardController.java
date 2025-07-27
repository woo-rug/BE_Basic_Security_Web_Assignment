package com.example.board.bulletinboard.controller;

import com.example.board.bulletinboard.domain.Board;
import com.example.board.bulletinboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 전체 조회 (GET /api/posts)
    @GetMapping
    public ResponseEntity<List<Board>> listBoards(){
        List<Board> boards = boardService.findAllBoards();
        return ResponseEntity.ok(boards);
    }

    // 단 건 조회 (GET /api/posts/{id}
    @GetMapping("/{postId}")
    public ResponseEntity<Board> viewBoard(@PathVariable long postId){
        return boardService.findBoardById(postId)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다. " + postId));
    }

    // 게시글 작성 (POST /api/posts)
    @PostMapping
    public ResponseEntity<Board> saveBoard(@RequestParam String title,
                                           @RequestParam String content,
                                           @RequestParam String writer,
                                           @RequestParam String password) {
        try {
            Board newBoard = boardService.createBoard(title, content, writer, password);
            return new ResponseEntity<>(newBoard, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 작성 실패 " + e.getMessage());
        }
    }

    // 게시글 수정 (PATCH api/posts/{postId}
    @PatchMapping("/{postId}")
    public ResponseEntity<Optional<Board>> updateBoard(
            @PathVariable Long postId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String password) {
        try{
            Optional<Board> updatedBoard = boardService.updateBoard(postId, title, content, password);
            return ResponseEntity.ok(updatedBoard);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 수정 실패 " + e.getMessage());
        }
    }

    // 게시글 삭제 (DELETE /api/posts/{postId})
    @DeleteMapping("/{postId}")
    public ResponseEntity<Map<String, String>> deleteBoard(@PathVariable Long postId,
                                                           @RequestParam String password) {
        try{
            boardService.deleteBoard(postId, password);
            return ResponseEntity.ok(Map.of("message", "게시글이 성공적으로 삭제되었습니다."));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 삭제 실패 " + e.getMessage());
        }
    }
}
