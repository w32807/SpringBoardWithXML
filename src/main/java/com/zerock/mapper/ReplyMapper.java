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
	// 댓글 페이징 (@Param에서 설정한 name이 Mybatis의 쿼리에서 #{name} 형태로 사용됨
	public List<ReplyVO> getListWithPaging(@Param ("cri") Criteria cri, @Param("bno") Long bno);
	// 페이징 처리를 위한 총 리스트 갯수 반환
	public int getCountByBno(Long bno);
}
