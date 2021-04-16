package com.zerock.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor // replyCnt�� list�� �����ڷ� ó���ϱ� ����
@Getter
//������ ���� ������ ������ �ִ� Criteria���� ���� �޾� ������ Paging ó���� �ϴ� ����� �̷�� ���� DTO
public class ReplyPageDTO {
	private int replyCnt;
	
	private List<ReplyVO> list;
}
