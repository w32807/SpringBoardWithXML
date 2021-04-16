package com.zerock.controller;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j;
import testConfig.ApplicationContextTest;

@Log4j
public class BoardControllerTests extends ApplicationContextTest{

    @Test
    public void nullTest() {
        assertNotNull(mockMvc);
    }
    
    @Test
    public void testList() throws Exception{
        log.info(
                mockMvc.perform(MockMvcRequestBuilders.get("/board/list"))
                .andReturn().getModelAndView().getModelMap()
                );
    }
    
    @Test
    public void testRegister() throws Exception{
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/register")
                .param("title", "테스트 새글 제목")
                .param("content", "테스트 새글 내용")
                .param("writer", "user00")).andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }
    
    @Test
    public void testGet() throws Exception{
        log.info(mockMvc.perform(MockMvcRequestBuilders.get("/board/get")
                .param("bno", "2")).andReturn().getModelAndView().getModelMap());
    }
    
    @Test
    public void testModify() throws Exception{
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/modify")
                .param("bno", "1")
                .param("title", "수정된 테스트 새글 제목")
                .param("content", "수정된 테스트 새글 내용")
                .param("writer", "user00")).andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }
    
    @Test
    public void testRemove() throws Exception{
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/remove")
                .param("bno", "25")).andReturn().getModelAndView().getViewName();
        log.info(resultPage);
    }
    
    @Test
    public void testListPaging() throws Exception{
    	log.info(mockMvc.perform(MockMvcRequestBuilders.get("/board/list").param("pageNum", "2")
    			.param("amount", "50")).andReturn().getModelAndView().getModelMap());
    }
}
