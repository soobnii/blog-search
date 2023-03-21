package com.soob.blog.apiserver.openapi.kakao.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MetaModel {
	
	private Integer totalCount;
	
	private Integer pageableCount;
	
	private Boolean isEnd;
	
}
