package com.example.board.bulletinboard.controller;

import com.example.board.bulletinboard.domain.Board;
import com.example.board.bulletinboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 전체 조회
    @GetMapping
    public String listBoards(Model model){
        List<Board> boards = boardService.findAllBoards();
        model.addAttribute("boards", boards);
        return "boardList";
    }

    // 단 건 조회
    @GetMapping("/{id}")
    public String viewBoard(Model model, @PathVariable long id, RedirectAttributes redirectAttributes){
        Optional<Board> board = boardService.findBoardById(id);
        if(board.isPresent()) {
            model.addAttribute("board", board.get());
            return "boardDetail";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 게시글이 없습니다.");
            return "redirect:/boards";
        }
    }

    // 새 글 작성
    @GetMapping("/new")
    public String newBoard(Model model){
        return "newBoard";
    }

    // 게시글 작성 처리
    @PostMapping
    public String saveBoard(@RequestParam String title,
                            @RequestParam String content,
                            @RequestParam String writer,
                            @RequestParam String password,
                            RedirectAttributes redirectAttributes) {
        try {
            boardService.createBoard(title, content, writer, password);
            redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 작성되었습니다.");
            return "redirect:/boards";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 작성 실패: " + e.getMessage());
            return "redirect:/boards/new"; // 실패 시 다시 작성 폼으로
        }
    }

    @GetMapping("/{id}/edit")
    public String editBoard(@PathVariable long id, Model model, RedirectAttributes redirectAttributes){
        Optional<Board> board = boardService.findBoardById(id);
        if(board.isPresent()) {
            model.addAttribute("board", board.get());
            return "editBoard";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 게시글이 없습니다.");
            return "redirect:/boards";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateBoard(@PathVariable Long id,
                              @RequestParam String title,
                              @RequestParam String content,
                              @RequestParam String password,
                              RedirectAttributes redirectAttributes) {
        try {
            boardService.updateBoard(id, title, content, password);
            redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 수정되었습니다.");
            return "redirect:/boards/" + id;
        } catch (IllegalArgumentException e) { // 비밀번호 오류 시
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/boards/" + id + "/edit";
        } catch (Exception e) { // 이외의 오류
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 수정 실패: " + e.getMessage());
            return "redirect:/boards/" + id + "/edit";
        }
    }

    // 게시글 삭제
    @PostMapping("/{id}/delete")
    public String deleteBoard(@PathVariable Long id,
                              @RequestParam String password, // 삭제 시 비밀번호 입력 받기
                              RedirectAttributes redirectAttributes) {
        try {
            boardService.deleteBoard(id, password);
            redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 삭제되었습니다.");
            return "redirect:/boards";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/boards/" + id; // 비밀번호 오류 시 상세 페이지로 다시
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 삭제 실패: " + e.getMessage());
            return "redirect:/boards/" + id;
        }
    }

}
