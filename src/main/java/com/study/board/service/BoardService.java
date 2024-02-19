package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    //글 작성 처리
    public void write(Board board){
        boardRepository.save(board);
    }

    public void saveImg(Board board, MultipartFile file) throws Exception {
        String projectPath = System.getProperty("user.dir") + "/src/main/webapp/files";
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // 파일 이름을 랜덤으로 생성

        // 이미지 파일을 디스크에 저장
        Files.copy(file.getInputStream(), Paths.get(projectPath).resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        // 엔티티에 파일 이름과 경로 설정
        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);
        boardRepository.save(board);

    }

    //게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {

        return boardRepository.findAll(pageable);

    }

    //페이지 검색 기능
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    //특정 게시글 불러오기
    public Board boardView(Integer id) {
        return boardRepository.findById(id).get();//해당하는 id의 글 불러옴
        //파일도 같이 불러오기안되나?
    }

    //특정 게시글 삭제
    public void boardDelete(Integer id) {  //게시글 번호가 서버에 들어왔을 때 해당id값의 글을 삭제합니다.
        boardRepository.deleteById(id);
    }
}
