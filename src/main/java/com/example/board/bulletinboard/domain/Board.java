package com.example.board.bulletinboard.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false)
    private Long id;

    @Column(name="title", nullable = false, length = 50)
    private String title;

    @Column(name="content", nullable = false)
    private String content;

    @Column(name="writer", nullable = false, length = 50)
    private String writer;

    @Column(name="password", nullable = false, length = 50)
    private String password;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Board(String title, String content, String writer, String password) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String content){
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public boolean verifyPassword(String inputPassword){
        return this.password.equals(inputPassword);
    }
}
