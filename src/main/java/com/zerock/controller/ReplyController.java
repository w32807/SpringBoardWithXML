package com.zerock.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zerock.domain.Criteria;
import com.zerock.domain.ReplyPageDTO;
import com.zerock.domain.ReplyVO;
import com.zerock.service.ReplyService;


@RestController
@RequestMapping("/replies/*")
// @RestController�� ����ϱ� ���ؼ��� JSOM/XML�� jackson-databind dependency�� �ʿ��ϴ�.
// Mapping ��, consumes�� ���� Ư�� Ÿ�Կ��� request�� �����ϵ���, produces�� ���� Ư�� Ÿ�Կ��� response�� �����ϵ��� ������ �� �ִ�.
// Mapping�� value ������ URI�� ������ ����.
// �� �޼ҵ��� ��ȯ���� ResponseEntity<T> ��, T���� Http �����ڵ带 ������ ��ȯ���� Ÿ�԰� ��ġ�ؾ� �Ѵ�.
public class ReplyController {
    
    @Autowired
    private ReplyService service;
    
    // consumes = "application/json", produces = {MediaType.TEXT_PLAIN_VALUE} �� �̿��� 
    // request�� JSON �����Ϳ��� �����ϵ���, response�� MediaType.TEXT_PLAIN_VALUE���� �����ϵ��� ����
    @PostMapping(value = "/new", consumes = "application/json", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> create (@RequestBody ReplyVO vo){
    	// @RequestBody�� �̿��Ͽ�, URI�� �����͸� ReplyVO�� ���ε� ��Ű���� ������
    	int insertCnt = service.register(vo);
    	// ��Ͽ� ���� ��, http header�� OK��, �����ϸ� error�� ������.
    	return insertCnt == 1 ? new ResponseEntity<>("success", HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @GetMapping(value = "/pages/{bno}/{page}"
    		,produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<ReplyPageDTO> getList (@PathVariable("page") int page, @PathVariable("bno") Long bno){
    	// @PathVariable�� URI���� ������ ���ε��ϴ� ���̱� ������, 
    	// value�� ������ ���� name�� @PathVariable�� name�� �������� ������ �޼ҵ� ��ü�� ������� �ʴ´�.
    	Criteria cri = new Criteria(page, 10);
    	return new ResponseEntity<>(service.getListPage(cri, bno), HttpStatus.OK);
    }
    
    @GetMapping(value = "/{rno}"
    		,produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<ReplyVO> get (@PathVariable("rno") Long rno){
    	return new ResponseEntity<>(service.get(rno), HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/{rno}" ,produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> remove (@PathVariable("rno") Long rno){
    	return service.remove(rno) == 1 ? 
    			new ResponseEntity<>("success", HttpStatus.OK) 
    			: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH}, value = "/{rno}"
    		,produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> modify (@RequestBody ReplyVO vo, @PathVariable("rno") Long rno){
    	vo.setRno(rno);
    	return service.modify(vo) == 1 ? 
    			new ResponseEntity<>("success", HttpStatus.OK) 
    			: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
