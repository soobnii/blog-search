package com.soob.blog.coreserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@Getter
@Table(indexes = {
		@Index(name = "idx_ksc_keyword", columnList = "searchKeyword"),
		@Index(name = "idx_ksc_cnt_moddatetime", columnList = "searchCount, modDateTime")
	})
@Entity
public class KeywordSearchCnt {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String searchKeyword;
	
	private LocalDateTime regDateTime = LocalDateTime.now();
	
	private LocalDateTime modDateTime = LocalDateTime.now();
	
	private Integer searchCount;
	
	public KeywordSearchCnt(String searchKeyword) {
		this.searchKeyword = searchKeyword;
		this.searchCount = 1;
	}
}
