package com.soob.blog.coreserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@Getter
@Table(indexes = {
		@Index(name = "idx_ksh_keyword", columnList = "searchKeyword"),
		@Index(name = "idx_ksh_regdate", columnList = "regDate")
	})
@Entity
public class KeywordSearchHistory {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private String searchKeyword;
	
	private LocalDate regDate = LocalDate.now();
	
	private LocalDateTime regDateTime = LocalDateTime.now();
	
	private String searchId;
	
	public KeywordSearchHistory(String keyword, String searchId) {
		this.searchKeyword = keyword;
		this.searchId = searchId;
	}
	
}
