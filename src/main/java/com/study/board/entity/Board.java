package com.study.board.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

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
    private String filepath;

    @Lob
    @Column(name = "mediaData", columnDefinition="LONGBLOB")
    private byte[] mediaData;
    public void setMediaData(MultipartFile file) {
        try {
            this.mediaData = file.getBytes(); // 이미지 파일을 byte 배열로 변환하여 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
