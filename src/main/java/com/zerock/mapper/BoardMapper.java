package com.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zerock.domain.BoardVO;
import com.zerock.domain.Criteria;

public interface BoardMapper {

    public List<BoardVO> getList();
    
    public List<BoardVO> getListWithPaging(Criteria cri);
    // ����¡ ó���� ���� �� ����Ʈ ���� ��ȯ
    public int getTotalCount(Criteria cri);
    
    public void insert(BoardVO board);
    
    public void insertSelectKey(BoardVO board);
    
    public BoardVO read(Long bno);
    
    public int delete(Long bno);
    
    public int update(BoardVO board);
    
    // Mybatis�� 2�� �̻��� �Ű������� ������ �� @Param�� ��� 
    public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
}
