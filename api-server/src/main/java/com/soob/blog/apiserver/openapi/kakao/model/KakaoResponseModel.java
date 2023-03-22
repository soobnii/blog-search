package com.soob.blog.apiserver.openapi.kakao.model;

import com.soob.blog.apiserver.keyword.dto.BlogDto;
import com.soob.blog.apiserver.keyword.dto.SearchResponseDto;
import lombok.Getter;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Getter
public class KakaoResponseModel {
	
	private MetaModel meta;
	
	private List<DocumentModel> documents;
	
	public SearchResponseDto toDto() {
		List<BlogDto> blogList = new ArrayList<>();
		this.documents.forEach(document ->
				blogList.add(BlogDto.builder()
								.blogname(document.getBlogname())
								.contents(document.getContents())
								.date(document.getDatetime().toInstant()
										.atZone(ZoneId.systemDefault())
										.toLocalDate())
								.thumbnail(document.getThumbnail())
								.title(document.getTitle())
								.url(document.getUrl())
								.build())
			);
		
		return SearchResponseDto.builder()
				.totalCount(this.meta.getTotalCount())
				.pageableCount(this.meta.getPageableCount())
				.blog(blogList)
				.build();
	}
}
