package com.zerock.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zerock.domain.BoardVO;
import com.zerock.domain.Criteria;

import lombok.extern.log4j.Log4j;
import testConfig.ApplicationContextTest;

@Log4j
public class BoardServiceTests extends ApplicationContextTest{
    
    @Autowired
    private BoardService service;
    
    @Test
    public void testExist() {
        assertNotNull(service);
    }
    
    @Test
    @Ignore
    public void testRegister() {
        BoardVO board = BoardVO.builder().title("새로 작성하는 글")
                .content("새로 작성하는 내용")      
                .writer("원준").build();
        service.register(board);
    }
    
    @Test
    public void testGetList() {
        //mapper.getList().forEach(board -> log.info(board));
    	service.getList(new Criteria(2, 10)).forEach(board -> log.info(board));
    }
    
    @Test
    public void testRead() {
        BoardVO board = boardMapper.read(5L);
    }
}
