package com.zerock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zerock.mapper.Sample1Mapper;
import com.zerock.mapper.Sample2Mapper;

@Service
public class SampleTxServiceImpl implements SampleTxService{
	
	@Autowired
	private Sample1Mapper mapper1;
	
	@Autowired
	private Sample2Mapper mapper2;

	@Override
	@Transactional
	public void addData(String value) {
		mapper1.insertCol1(value);
		mapper2.insertCol2(value);
	}
}
