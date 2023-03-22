package com.soob.blog.coreserver.repository;

import com.soob.blog.coreserver.entity.KeywordSearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface KeywordRepository extends JpaRepository<KeywordSearchHistory, Long> {

	List<KeywordSearchHistory> findAllByRegDate(LocalDate date);
}
