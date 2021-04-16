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
// @RestController를 사용하기 위해서는 JSOM/XML과 jackson-databind dependency가 필요하다.
// Mapping 시, consumes를 통해 특정 타입에만 request가 응답하도록, produces를 통해 특정 타입에만 response가 응답하도록 지정할 수 있다.
// Mapping의 value 값에는 URI의 형식이 들어간다.
// 각 메소드의 반환형인 ResponseEntity<T> 중, T에는 Http 응답코드를 제외한 반환형의 타입과 일치해야 한다.
public class ReplyController {
    
    @Autowired
    private ReplyService service;
    
    // consumes = "application/json", produces = {MediaType.TEXT_PLAIN_VALUE} 를 이용해 
    // request는 JSON 데이터에만 응답하도록, response는 MediaType.TEXT_PLAIN_VALUE에만 응답하도록 지정
    @PostMapping(value = "/new", consumes = "application/json", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> create (@RequestBody ReplyVO vo){
    	// @RequestBody를 이용하여, URI의 데이터를 ReplyVO에 바인딩 시키도록 지정함
    	int insertCnt = service.register(vo);
    	// 등록에 성공 시, http header에 OK를, 실패하면 error를 던진다.
    	return insertCnt == 1 ? new ResponseEntity<>("success", HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @GetMapping(value = "/pages/{bno}/{page}"
    		,produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<ReplyPageDTO> getList (@PathVariable("page") int page, @PathVariable("bno") Long bno){
    	// @PathVariable는 URI에서 변수를 바인딩하는 것이기 때문에, 
    	// value의 변수가 들어가는 name과 @PathVariable의 name이 동일하지 않으면 메소드 자체가 실행되지 않는다.
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
