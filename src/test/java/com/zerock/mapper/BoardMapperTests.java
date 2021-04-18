package com.zerock.mapper;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.zerock.domain.BoardVO;
import com.zerock.domain.Criteria;

import lombok.extern.log4j.Log4j;
import testConfig.ApplicationContextTest;

@Log4j
public class BoardMapperTests extends ApplicationContextTest{
	
    @Test
    public void testNullChk() {
        assertNotNull(boardMapper);
    }
    @Test
    @Ignore
    public void testGetList() {
        boardMapper.getList().forEach(board -> log.info(board));
    }
    
    @Test
    @Ignore
    public void testInsert() {
        BoardVO board = BoardVO.builder().title("새로 작성하는 글")
                                                                .content("새로 작성하는 내용")      
                                                                .writer("원준").build();
        boardMapper.insert(board);
        
        log.info(board);
    }
    
    @Test
    @Ignore
    public void testRead() {
        BoardVO board = boardMapper.read(5L);
        log.info(board);
    }
    
    @Test
    @Ignore
    public void testDelete() {
        log.info("delete >> " + boardMapper.delete(5L));
    }
    
    @Test
    @Ignore
    public void testPaging() {
    	Criteria cri = new Criteria();
    	cri.setPageNum(3);
    	cri.setAmount(10);
    	
    	List<BoardVO> list = boardMapper.getListWithPaging(cri);
    	
    	list.forEach(board -> log.info(board));
    }
    
    @Test
    public void testSearch() {
    	Criteria cri = new Criteria();
    	cri.setKeyword("새로");
    	cri.setType("TC");
    	
    	List<BoardVO> list = boardMapper.getListWithPaging(cri);
    	list.forEach(board -> log.info(board));
    }
}
