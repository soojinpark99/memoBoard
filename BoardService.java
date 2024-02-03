package com.study.board.service;

import com.study.board.BoardApplication;
import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    //글 작성 처리
    public void write(Board board) {
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
        return BoardRepository.findById(id); //해당하는 id의 글 불러옴
    }

    //특정 게시글 삭제
    public void boardDelete(Integer id) {  //게시글 번호가 서버에 들어왔을 때 해당id값의 글을 삭제합니다.
        BoardRepository.deleteById(id).get();
    }
}
