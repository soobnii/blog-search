package com.soob.blog.coreserver.repository;

import com.soob.blog.coreserver.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
	
	Optional<Keyword> findByKeyword(String keyword);
	
	Optional<List<Keyword>> findTop10ByOrderBySearchCountDesc();
}
