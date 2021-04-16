package com.zerock.domain;


import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {
	// Criteria�� �˻��� �����̶�� ��
    private int pageNum; // ������ ��ȣ
    private int amount;// 1�������� ��� �����͸� ������ ����
    
    private String type; // �˻�Ÿ��
    private String keyword; // Ű����
    
    public Criteria() {
    	this(1, 10);
	}
    
    public Criteria(int pageNum, int amount) {
    	this.pageNum = pageNum;
    	this.amount = amount;
	}
    
    public String[] getTypeArr() {
    	return type == null ? new String[] {} : type.split("");
    }
    
    // �Ź� ����¡, �˻��� �Ķ���͸� ���������� �����ϴ� ���� ���ŷο��� 
    // UriComponentsBuilder�� �̿�����.
    // UriComponentsBuilder�� ���� ���� �Ķ���͵��� �����ؼ� URL�� ���·� ������ִ� ����� �Ѵ�.
    public String getListLink() {
    	UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
    			.queryParam("pageNum", this.getPageNum())
    			.queryParam("amount", this.getAmount())
    			.queryParam("type", this.getType())
    			.queryParam("keyword", this.getKeyword());
		return builder.toUriString();
	}
}
