package com.study.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity //zㅋㅋㅋㅋㅋ
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //board db에 대한 정보를 읽습니다.
    private Integer id;
    private String title;
    private String content;
}
