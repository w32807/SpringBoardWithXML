package com.zerock.mapper;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Ignore;
import org.junit.Test;

import com.zerock.domain.Criteria;
import com.zerock.domain.ReplyVO;

import lombok.extern.log4j.Log4j;
import testConfig.ApplicationContextTest;

@Log4j
public class ReplyMapperTests extends ApplicationContextTest{
	private Long[] bnoArr = {1267L , 1372L, 1875L, 1919L, 1920L };
	
    @Test
    public void testNullChk() {
        assertNotNull(replyMapper);
    }
    
    @Test
    @Ignore
    public void testCreate() {
    	
    	IntStream.rangeClosed(1, 10).forEach(i -> {
    		ReplyVO vo = new ReplyVO();
    		
    		vo.setBno(bnoArr[i%5]);
    		vo.setReply("댓글 테스트" +i);
    		vo.setReplyer("replyer"+i);
    		
    		replyMapper.insert(vo);
    	});
    }
    
    @Test
    @Ignore
    public void testRead() {
    	log.info(replyMapper.read(3L));
    }
    
    @Test
    @Ignore
    public void testDelete() {
    	log.info(replyMapper.delete(3L));
    }
    
    @Test
    @Ignore
    public void testUpdate() {
    	ReplyVO vo = replyMapper.read(4L);
    	vo.setReply("UPDATE 테스트");
    	replyMapper.update(vo);
    	log.info(replyMapper.read(4L));
    }
    
    @Test
    @Ignore
    public void testList() {
    	Criteria cri = new Criteria();
    	List<ReplyVO> replies = replyMapper.getListWithPaging(cri, bnoArr[0]);
    	replies.forEach(reply -> log.info(reply));
    }
    
    @Test
    public void testList2() {
    	Criteria cri = new Criteria(2, 10);
    	List<ReplyVO> replies = replyMapper.getListWithPaging(cri, 1372L);
    	replies.forEach(reply -> log.info(reply));
    }
}
