package com.example.board.bulletinboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardUpdateRequest {
    private String title;
    private String content;
    private String password;

    public BoardUpdateRequest(String title, String content, String password) {
        this.title = title;
        this.content = content;
        this.password = password;
    }
}
