package com.soob.blog.apiserver.keyword.service.impl;

import com.soob.blog.apiserver.openapi.kakao.KakaoService;
import com.soob.blog.apiserver.openapi.naver.NaverService;
import com.soob.blog.apiserver.keyword.dto.KeywordDto;
import com.soob.blog.apiserver.keyword.dto.SearchRequestDto;
import com.soob.blog.apiserver.keyword.dto.SearchResponseDto;
import com.soob.blog.apiserver.keyword.service.KeywordService;
import com.soob.blog.coreserver.entity.Keyword;
import com.soob.blog.coreserver.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {
	
	private final KakaoService kakaoService;
	
	private final NaverService naverService;
	
	private final KeywordRepository keywordRepository;
	
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public SearchResponseDto findBlog(SearchRequestDto requestDto) {
		
		SearchResponseDto responseDto = null;
		String oepnApi  = requestDto.getOepnApi();
		if(!StringUtils.hasText(oepnApi) || oepnApi.equals("kakao")) {
			responseDto = kakaoService.searchBlog(requestDto);
		} else if(oepnApi.equals("naver")) {
			responseDto = naverService.searchBlog(requestDto);
		}
		this.updateKeyword(requestDto.getQuery());
		return responseDto;
	}
	
	@Override
	public List<KeywordDto> findKeyword() {
		List<KeywordDto> list = new ArrayList<>();
		Optional<List<Keyword>> optionalList = keywordRepository.findTop10ByOrderBySearchCountDesc();
		if(optionalList.isPresent()) {
			List<Keyword> keywords = optionalList.get();
			for (Keyword key : keywords) {
				list.add(new KeywordDto(key.getKeyword(), key.getSearchCount()));
			}
		}
		return list;
	}
	
	private void updateKeyword(String searchKeyword) {
		String[] keywords = searchKeyword.split(" ");
		List<Keyword> keywordList = new ArrayList<>();
		for (String key : keywords) {
			key = key.trim();
			if(StringUtils.hasText(key)) {
				Keyword keyword = new Keyword(key);
				Optional<Keyword> optional = keywordRepository.findByKeyword(key);
				if(optional.isPresent()) {
					optional.get().addCount();
					optional.get().updateTime();
					keywordList.add(optional.get());
				} else {
					keyword.addCount();
					keywordList.add(keyword);
				}
			}
		}
		keywordRepository.saveAll(keywordList);
	}
}
