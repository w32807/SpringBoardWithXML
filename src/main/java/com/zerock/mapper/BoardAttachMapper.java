package com.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zerock.domain.BoardAttachVO;
import com.zerock.domain.BoardVO;
import com.zerock.domain.Criteria;

public interface BoardAttachMapper {

    public void insert(BoardAttachVO board);
    
    public int delete(String uuid);
    
    public List<BoardAttachVO> findByBno(Long bno);
    
}
