package com.zerock.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
// 페이지 관련 정보를 가지고 있는 Criteria에서 값을 받아 실제로 Paging 처리를 하는 계산이 이루어 지는 DTO
public class PageDTO {
	private int startPage;
	private int endPage;
	private boolean prev, next;
	
	private int total;
	private Criteria cri;
	
	public PageDTO(Criteria cri, int total) {
		this.cri = cri;
		this.total = total;
		
		this.endPage = (int) (Math.ceil(cri.getPageNum() / 10.0)) * 10;
		this.startPage = this.endPage - 9;
		
		int realEnd = (int) (Math.ceil((total * 1.0) / cri.getAmount()));
		
		this.endPage = (realEnd < this.endPage) ? realEnd : this.endPage;
		this.prev = this.startPage > 1;
		this.next = this.endPage < realEnd;
	}
}
