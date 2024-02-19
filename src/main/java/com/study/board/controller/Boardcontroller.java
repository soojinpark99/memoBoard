package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;


@Controller

public class Boardcontroller {
    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write")//localhost:8080/board/write
    public String boardWriteForm() {
        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, @RequestParam("file") MultipartFile file) {


        try {
            // 이미지 파일을 byte 배열로 변환하여 엔티티에 저장
            board.setMediaData(file);
            boardService.write(board);
            boardService.saveImg(board,file);
            // 게시글과 이미지 정보를 저장
            boardService.write(board);

            model.addAttribute("message", "글 작성이 완료되었습니다.");
            model.addAttribute("searchUrl", "/board/list");
            return "message";
        } catch (Exception e) {
            model.addAttribute("error", "[ERROR] : 글 작성에 실패했습니다. 이미지 파일 형식이 올바르지 않을 수도 있습니다.");
            return "error";
        }

    }

    //게시글 리스트 처리
    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page=0, size=10, sort="id", direction = Sort.Direction.DESC)
                            Pageable pageable, String searchKeyword)
    //디폴트 페이지:0, 한 페이지당 게시글 수:10개, 정렬 기준 방식: id, 정렬 순서:역순
    {
        Page<Board> list = null;

        if(searchKeyword == null) {
            list = boardService.boardList(pageable);} //검색 단어 미입력: 기존 리스트 그대로 출력
        else {
            list = boardService.boardSearchList(searchKeyword, pageable); //검색 단어 입력 : 검색 기능이 포함된 리스트를 출력
        }

        int nowPage = list.getPageable().getPageNumber()+1; //Pageable에서 넘어온 현재 페이지. 시작값이 0이라 1을 더함.
        int startPage = Math.max(nowPage - 4, 1); //블럭에서 보여줄 시작 페이지. Math.max로 페이지번호가 음수가 되는 오류 수정.
        int lastPage = list.getTotalPages();
        int endPage = Math.min(nowPage + 5, lastPage); //블럭에서 보여줄 마지막 페이지. Math.min으로 페이지번호가 전체 페이지수보다 클 경우 totalpages를 반환.
        
        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, MultipartFile file) throws Exception{

        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp);

        return "redirect:/board/list";

    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id,
                              Model model) {
        model.addAttribute("board",boardService.boardView(id));
        return "boardmodify";
    }

    @GetMapping("/board/view") // localhost:8080/board/view?id=1
    public String boardView(Model model, Integer id){
        Board board = boardService.boardView(id);
        String base64Image = null;
        if (board.getMediaData() != null) {
            base64Image = Base64.getEncoder().encodeToString(board.getMediaData());
        }
        model.addAttribute("board", board);
        model.addAttribute("base64Image", base64Image);
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {
        boardService.boardDelete(id);
        return "redirect:/board/list"; //원하는 페이지를 삭제한 후에는 리스트 페이지로 되돌아갑니다.
    }
}
