package com.zerock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    	// ����¡ ������ �˻��� ������ list�� ���� �� cri�� �ʵ��� ���ε�
    	// 1. cri ������ DB���� ����¡�� ���� �����͸� ������
        model.addAttribute("list", service.getList(cri));
        // ȭ�鿡�� ����¡ ó���� ���� ������ ��ü ����
        //model.addAttribute("pageMaker", new PageDTO(cri, 123));
        // 2. ȭ�鿡 ���� ������ ������ ���� �� ������ ��ȣ UI�� �����ϱ� ���� PageDTO�� ����
        model.addAttribute("pageMaker", new PageDTO(cri, service.getTotal(cri)));
    }
    
    @GetMapping("/register")
    public void register() {}
    
    @PostMapping("/register")
    public String register(BoardVO board, RedirectAttributes rttr) {
        service.register(board);
        rttr.addFlashAttribute("result", board.getBno());
        return "redirect:/board/list";
    }
    
    @GetMapping({"/get", "modify"})
    public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, Model model) {
    	// @ModelAttribute�� �ڵ����� ������ �̸����� model�� �־��ش�.
    	// ���⼭�� JSP���� pageNum, amount�� �޾Ƽ� cri �� ���ε� ��, Model�� cri �̸����� �־��ִ� �۾����� ����ȴ�.
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
        // ���� �ּ� �κ��� ���ְ�, cri.getListLink()�� get����� URL�� ����������
        return "redirect:/board/list" + cri.getListLink();
    }
    
    @PostMapping("remove")
    public String remove(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
        if(service.remove(bno)) rttr.addFlashAttribute("result", "sucess");
        return "redirect:/board/list" + cri.getListLink();
    }
}
