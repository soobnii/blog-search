package com.soob.blog.apiserver.keyword.service.impl;

import com.soob.blog.apiserver.exception.BadRequestException;
import com.soob.blog.apiserver.exception.InternalServerErrorException;
import com.soob.blog.apiserver.keyword.dto.KeywordDto;
import com.soob.blog.apiserver.keyword.dto.KeywordHistoryDto;
import com.soob.blog.apiserver.keyword.dto.SearchRequestDto;
import com.soob.blog.apiserver.keyword.dto.SearchResponseDto;
import com.soob.blog.apiserver.keyword.service.KeywordService;
import com.soob.blog.apiserver.openapi.kakao.KakaoService;
import com.soob.blog.apiserver.openapi.naver.NaverService;
import com.soob.blog.coreserver.entity.KeywordSearchCnt;
import com.soob.blog.coreserver.entity.KeywordSearchHistory;
import com.soob.blog.coreserver.repository.KeywordRepository;
import com.soob.blog.coreserver.repository.KeywordSearchCntRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {
	
	private final KakaoService kakaoService;
	
	private final NaverService naverService;
	
	private final KeywordRepository keywordRepository;
	
	private final KeywordSearchCntRepository keywordSearchCntRepository;
	
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public SearchResponseDto findBlog(String searchId, SearchRequestDto requestDto) {
		
		SearchResponseDto responseDto = null;
		String type  = requestDto.getType();
		if(!StringUtils.hasText(type) || type.equals("kakao")) {
			responseDto = kakaoService.searchBlog(requestDto);
		} else if(type.equals("naver")) {
			responseDto = naverService.searchBlog(requestDto);
		} else {
			throw new BadRequestException("지원하지 않는 타입입니다.");
		}
		this.updateKeyword(searchId, requestDto.getQuery());
		return responseDto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<KeywordDto> findKeyword() {
		List<KeywordDto> dtoList = new ArrayList<>();
		List<KeywordSearchCnt> keyList= keywordSearchCntRepository.findTop10ByOrderBySearchCountDescModDateTimeDesc();
		for (KeywordSearchCnt key : keyList) {
			dtoList.add(new KeywordDto(key.getSearchKeyword(), key.getSearchCount()));
		}
		return dtoList;
	}
	
	@Override
	public List<KeywordHistoryDto> findKeywordHistory(String searchDate) {
		List<KeywordHistoryDto> dtoList = new ArrayList<>();
		LocalDate date = LocalDate.now();
		if(StringUtils.hasText(searchDate)) {
			try {
				date = LocalDate.parse(searchDate);
			} catch (Exception exception) {
				throw new BadRequestException("입력한 날짜를 확인해주세요. 형식은 yyyy-MM-dd 이어야 합니다.");
			}
		}
		List<KeywordSearchHistory> list = keywordRepository.findAllByRegDate(date);
		for (KeywordSearchHistory history : list) {
			dtoList.add(new KeywordHistoryDto(history));
		}
		return dtoList;
	}
	
	private void updateKeyword(String searchId, String searchKeyword) {
		String[] keywords = searchKeyword.split(" ");
		List<KeywordSearchHistory> keywordList = new ArrayList<>();
		
		try {
			for (String key : keywords) {
				key = key.trim();
				if(StringUtils.hasText(key)) {
					KeywordSearchHistory keyword = new KeywordSearchHistory(key, searchId);
					keywordList.add(keyword);
					
					Optional<KeywordSearchCnt> optional = keywordSearchCntRepository.findBySearchKeyword(key);
					if(optional.isPresent()) {
						KeywordSearchCnt keywordSearchCnt = optional.get();
						keywordSearchCntRepository.updateCount(keywordSearchCnt.getId(), LocalDateTime.now());
					} else {
						KeywordSearchCnt keywordSearchCnt = new KeywordSearchCnt(key);
						keywordSearchCntRepository.save(keywordSearchCnt);
					}
				}
			}
			keywordRepository.saveAll(keywordList);
		} catch (Exception e) {
			throw new InternalServerErrorException("잠시후 다시 시도해주세요.");
		}
		
	}
}
