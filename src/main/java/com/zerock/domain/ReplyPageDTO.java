package com.zerock.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor // replyCnt와 list를 생성자로 처리하기 위함
@Getter
//페이지 관련 정보를 가지고 있는 Criteria에서 값을 받아 실제로 Paging 처리를 하는 계산이 이루어 지는 DTO
public class ReplyPageDTO {
	private int replyCnt;
	
	private List<ReplyVO> list;
}
