package com.zerock.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//Mybatis�� List<ReplyVO> �� �� �����ڸ� �������� �������Ƿ�, �Ʒ� �� ���� ������̼� ���� 
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
