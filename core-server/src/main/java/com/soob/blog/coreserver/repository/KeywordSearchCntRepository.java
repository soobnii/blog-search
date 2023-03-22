package com.soob.blog.coreserver.repository;

import com.soob.blog.coreserver.entity.KeywordSearchCnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface KeywordSearchCntRepository extends JpaRepository<KeywordSearchCnt, Long> {
	
	Optional<KeywordSearchCnt> findBySearchKeyword(String keyword);
	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update KeywordSearchCnt k set k.searchCount = k.searchCount + 1 , k.modDateTime = :dateTime where k.id = :id")
	void updateCount(Long id, LocalDateTime dateTime);
	
	List<KeywordSearchCnt> findTop10ByOrderBySearchCountDescModDateTimeDesc();
}

