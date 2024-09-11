package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.config.PaginationProperties;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.NewsResponse;
import com.thanhdat.quanlyhoctap.dto.response.NewsViewResponse;
import com.thanhdat.quanlyhoctap.entity.News;
import com.thanhdat.quanlyhoctap.repository.NewsRepository;
import com.thanhdat.quanlyhoctap.service.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final PaginationProperties paginationProperties;

    public DataWithCounterDto<NewsResponse> getAll(Map<String, String> params) {
        Integer page = 0;
        Integer pageSize = paginationProperties.getPageSize();

        if (params.containsKey("page"))
            page = Integer.parseInt(params.get("page")) - 1;

        Pageable paging = PageRequest.of(page, pageSize);

        Page<News> pageNews = newsRepository.findAll(paging);
        Integer total = (int) pageNews.getTotalElements();
        List<NewsResponse> newsDto = mapToNewsDto(pageNews.getContent());
        return new DataWithCounterDto(newsDto, total);
    }

    public NewsViewResponse get(Integer id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        return mapToNewsViewDto(news);
    }

    private NewsViewResponse mapToNewsViewDto(News news) {
        return NewsViewResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .createdAt(news.getCreatedAt())
                .build();
    }

    private List<NewsResponse> mapToNewsDto(List<News> news) {
        return news.stream().map(n -> NewsResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .createdAt(n.getCreatedAt())
                .isImportant(n.getIsImportant())
                .build())
        .collect(Collectors.toList());
    }
}
