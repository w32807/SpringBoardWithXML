package com.zerock.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//Mybatis의 List<ReplyVO> 일 때 생성자를 기준으로 가져오므로, 아래 두 가지 어노테이션 선언 
@NoArgsConstructor
@AllArgsConstructor
public class ReplyVO {
	private Long rno;
    private Long bno;
    
    private String reply;
    private String replyer;
    private Date replyDate;
    private Date updateDate;
}
