package com.zerock.mapper;

import java.util.List;

import com.zerock.domain.BoardAttachVO;

public interface BoardAttachMapper {

    public void insert(BoardAttachVO board);
    
    public int delete(String uuid);
    
    public List<BoardAttachVO> findByBno(Long bno);
    
    public void deleteAll(Long bno);
    
    public List<BoardAttachVO> getOldFiles();
}
