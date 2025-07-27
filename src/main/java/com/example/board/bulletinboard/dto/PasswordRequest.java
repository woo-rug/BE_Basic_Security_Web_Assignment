package com.example.board.bulletinboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordRequest {
    private String password;

    public PasswordRequest(String password) {
        this.password = password;
    }
}
