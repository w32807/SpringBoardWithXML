package com.zerock.domain;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
// Mybatis�� List<BoardVO> �� �� �����ڸ� �������� �������Ƿ�, �Ʒ� �� ���� ������̼� ���� 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardVO {
    private Long bno;
    private String title;
    private String content;
    private String writer;
    private Date regDate;
    private Date updateDate;
    private int replyCnt;
    
    private List<BoardAttachVO> attachList;
}
