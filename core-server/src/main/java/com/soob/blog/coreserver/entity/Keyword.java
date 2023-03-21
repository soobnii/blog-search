package com.soob.blog.coreserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@Getter
@Table(name = "keyword", indexes = {
		@Index(name = "idx_keyword", columnList = "keyword"),
		@Index(name = "idx_searchcount", columnList = "searchCount")
})
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Keyword {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private String keyword;
	
	private Integer searchCount;
	
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime registrationDate = LocalDateTime.now();
	
	@LastModifiedDate
	private LocalDateTime modificationDate = LocalDateTime.now();
	
	public Keyword(String keyword) {
		this.keyword = keyword;
	}
	
	public void addCount() {
		if(this.searchCount != null) {
			this.searchCount = this.searchCount + 1;
		} else {
			this.searchCount = 1;
		}
	}
	
	public void updateTime() {
		this.modificationDate = LocalDateTime.now();
	}
}
