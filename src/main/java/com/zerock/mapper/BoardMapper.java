package com.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zerock.domain.BoardVO;
import com.zerock.domain.Criteria;

public interface BoardMapper {

    public List<BoardVO> getList();
    
    public List<BoardVO> getListWithPaging(Criteria cri);
    // 페이징 처리를 위한 총 리스트 갯수 반환
    public int getTotalCount(Criteria cri);
    
    public void insert(BoardVO board);
    
    public void insertSelectKey(BoardVO board);
    
    public BoardVO read(Long bno);
    
    public int delete(Long bno);
    
    public int update(BoardVO board);
    
    // Mybatis에 2개 이상의 매개변수를 전달할 때 @Param을 사용 
    public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
}
