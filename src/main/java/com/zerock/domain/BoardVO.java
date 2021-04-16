package com.zerock.domain;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
// Mybatis의 List<BoardVO> 일 때 생성자를 기준으로 가져오므로, 아래 두 가지 어노테이션 선언 
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
