package com.thanhdat.quanlyhoctap.mapper;

import com.thanhdat.quanlyhoctap.dto.response.NewsCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.NewsResponse;
import com.thanhdat.quanlyhoctap.dto.response.NewsViewCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.NewsViewResponse;
import com.thanhdat.quanlyhoctap.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    NewsResponse toNewsResponse(News news);
    NewsViewResponse toNewsViewResponse(News news);

    @Mapping(target = "authorName", source = "author.fullName")
    NewsCrudResponse toNewsCrudResponse(News news);

    @Mapping(target = "authorId", source = "author.id")
    NewsViewCrudResponse toNewsViewCrudResponse(News news);
}
