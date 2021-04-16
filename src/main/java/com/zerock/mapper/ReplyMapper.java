package com.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zerock.domain.Criteria;
import com.zerock.domain.ReplyVO;

public interface ReplyMapper {
	
	public int insert(ReplyVO vo);
	
	public ReplyVO read(Long bno);
	
	public int delete(Long rno);
	
	public int update(ReplyVO vo);
	// ��� ����¡ (@Param���� ������ name�� Mybatis�� �������� #{name} ���·� ����
	public List<ReplyVO> getListWithPaging(@Param ("cri") Criteria cri, @Param("bno") Long bno);
	// ����¡ ó���� ���� �� ����Ʈ ���� ��ȯ
	public int getCountByBno(Long bno);
}
