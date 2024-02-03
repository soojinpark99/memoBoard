package com.study.board.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
public class Board {
    //엔티티 매니저

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //board db에 대한 정보를 읽습니다.
    private Integer id;
    private String title;
    private String content;
    private String filename;
    private String filepath;}
