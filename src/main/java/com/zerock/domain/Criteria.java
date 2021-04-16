package com.zerock.domain;


import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {
	// Criteria는 검색의 기준이라는 뜻
    private int pageNum; // 페이지 번호
    private int amount;// 1페이지에 몇개의 데이터를 보여줄 건지
    
    private String type; // 검색타입
    private String keyword; // 키워드
    
    public Criteria() {
    	this(1, 10);
	}
    
    public Criteria(int pageNum, int amount) {
    	this.pageNum = pageNum;
    	this.amount = amount;
	}
    
    public String[] getTypeArr() {
    	return type == null ? new String[] {} : type.split("");
    }
    
    // 매번 페이징, 검색어 파라미터를 페이지마다 유지하는 일이 번거로워서 
    // UriComponentsBuilder를 이용하자.
    // UriComponentsBuilder는 여러 개의 파라미터들을 연결해서 URL의 형태로 만들어주는 기능을 한다.
    public String getListLink() {
    	UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
    			.queryParam("pageNum", this.getPageNum())
    			.queryParam("amount", this.getAmount())
    			.queryParam("type", this.getType())
    			.queryParam("keyword", this.getKeyword());
		return builder.toUriString();
	}
}
