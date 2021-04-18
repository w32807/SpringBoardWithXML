package com.zerock.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zerock.domain.BoardAttachVO;
import com.zerock.domain.BoardVO;
import com.zerock.domain.Criteria;
import com.zerock.domain.PageDTO;
import com.zerock.service.BoardService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/board/*")
@Log4j
public class BoardController {
    
    @Autowired
    private BoardService service;
    
    @GetMapping("/list")
    public void list(Criteria cri, Model model) {
    	// 페이징 정보와 검색어 정보가 list로 들어올 때 cri의 필드들과 매핑됨
    	// 1. cri 가지고 DB에서 페이징에 따른 데이터만 가져옴
        model.addAttribute("list", service.getList(cri));
        // 화면에서 페이징 처리를 위한 페이지 객체 전달
        //model.addAttribute("pageMaker", new PageDTO(cri, 123));
        // 2. 화면에 현재 페이지 정보를 전달 및 페이지 번호 UI를 구현하기 위해 PageDTO를 전달
        model.addAttribute("pageMaker", new PageDTO(cri, service.getTotal(cri)));
    }
    
    @GetMapping("/register")
    public void register() {}
    
    @PostMapping("/register")
    public String register(BoardVO board, RedirectAttributes rttr) {
    	if(board.getAttachList() != null) {
    		board.getAttachList().forEach(attach -> log.info(attach));
    	}
    	
        service.register(board);
        rttr.addFlashAttribute("result", board.getBno());
        return "redirect:/board/list";
    }
    
    @GetMapping({"/get", "modify"})
    public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, Model model) {
    	// @ModelAttribute는 자동으로 지정한 이름으로 model에 넣어준다.
    	// 여기서는 JSP에서 pageNum, amount를 받아서 cri 에 바인딩 후, Model에 cri 이름으로 넣어주는 작업까지 진행된다.
        model.addAttribute("board", service.get(bno));
    }
    
    @PostMapping("/modify")
    public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
        if(service.modify(board)) rttr.addFlashAttribute("result", "sucess");
        /*
        rttr.addAttribute("pageNum", cri.getPageNum());
        rttr.addAttribute("amount", cri.getAmount());
        rttr.addAttribute("type", cri.getType());
        rttr.addAttribute("keyword", cri.getKeyword());
        */
        // 위의 주석 부분을 없애고, cri.getListLink()로 get방식의 URL로 전달해주자
        return "redirect:/board/list" + cri.getListLink();
    }
    
    @PostMapping("remove")
    public String remove(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
    	List<BoardAttachVO> attachList = service.getAttachList(bno);
        if(service.remove(bno)) {
        	deleteFiles(attachList);
        	rttr.addFlashAttribute("result", "sucess");
        }
        return "redirect:/board/list" + cri.getListLink();
    }
    
    @GetMapping(value = "/getAttachList" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno){
    	return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK);
    }
    
    private void deleteFiles(List<BoardAttachVO> attachList) {
    	if(attachList == null || attachList.size() == 0) return;
		attachList.forEach(attach -> {
			try {
				Path file = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\" + attach.getUuid() + "_" + attach.getFileName());
				Files.deleteIfExists(file);
				if(Files.probeContentType(file).startsWith("image")) {
					Path thumbNail = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_" + attach.getFileName());
					Files.deleteIfExists(file);
				}
			} catch (Exception e) {
			}
		});
	}
}
