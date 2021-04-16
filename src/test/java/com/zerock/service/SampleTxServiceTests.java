package com.zerock.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j;
import testConfig.ApplicationContextTest;

@Log4j
public class SampleTxServiceTests extends ApplicationContextTest{

    @Test
    public void nullTest() {
        assertNotNull(txService);
    }
    
    @Test
    public void testLong() {
    	String str = "Starry \r\n Starry night \r\n paing your palette blue and gray \r\n Look out on a summer's day";
    	log.info(str.getBytes().length);
    	txService.addData(str);
    }
    
}
