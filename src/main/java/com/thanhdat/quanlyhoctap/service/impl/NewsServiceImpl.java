package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.config.PaginationProperties;
import com.thanhdat.quanlyhoctap.dto.request.NewsCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.repository.NewsRepository;
import com.thanhdat.quanlyhoctap.repository.StaffRepository;
import com.thanhdat.quanlyhoctap.service.NewsService;
import com.thanhdat.quanlyhoctap.util.PagingHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewsServiceImpl implements NewsService {
    NewsRepository newsRepository;
    StaffRepository staffRepository;
    PaginationProperties paginationProperties;
    PagingHelper pagingHelper;

    @Override
    public void create(NewsCrudRequest createRequest) {
        if (createRequest.getAuthorId() == null)
            throw new RuntimeException("Author id is required");

        Staff author = staffRepository.findById(createRequest.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        News newNews = News.builder()
                .title(createRequest.getTitle())
                .content(createRequest.getContent())
                .isImportant(createRequest.getIsImportant())
                .author(author)
                .createdAt(LocalDateTime.now())
                .build();
        newsRepository.save(newNews);
    }

    @Override
    public void delete(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public NewsViewCrudResponse getById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        return mapToNewsViewCrudDto(news);
    }

    private NewsViewCrudResponse mapToNewsViewCrudDto(News news) {
        return NewsViewCrudResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .authorId(news.getAuthor().getId())
                .isImportant(news.getIsImportant())
                .createdAt(news.getCreatedAt())
                .build();
    }

    @Override
    public void update(Long id, NewsCrudRequest updateRequest) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));

        news.setTitle(updateRequest.getTitle());
        news.setContent(updateRequest.getContent());
        news.setIsImportant(updateRequest.getIsImportant());

        Boolean isAuthorChanged = !news.getAuthor().getId().equals(updateRequest.getAuthorId());
        if (isAuthorChanged) {
            Staff author = staffRepository.findById(updateRequest.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Staff not found"));
            news.setAuthor(author);
        }

        newsRepository.save(news);
    }

    @Override
    public DataWithCounterDto<NewsCrudResponse> getAllCrud(Map<String, String> params) {
        Pageable paging = pagingHelper.getPageable(params);
        Page<News> page = newsRepository.findAll(paging);
        List<NewsCrudResponse> dto = page.getContent().stream()
                .map(this::mapToNewsCrudResponse)
                .collect(Collectors.toList());
        long total = page.getTotalElements();
        return new DataWithCounterDto<>(dto, total);
    }

    private NewsCrudResponse mapToNewsCrudResponse(News news) {
        return NewsCrudResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .isImportant(news.getIsImportant())
                .createdAt(news.getCreatedAt())
                .authorName(news.getAuthor().getFullName())
                .build();
    }

    public DataWithCounterDto<NewsResponse> getAll(Map<String, String> params) {
        Integer page = 0;
        Integer pageSize = paginationProperties.getPageSize();

        if (params.containsKey("page"))
            page = Integer.parseInt(params.get("page")) - 1;

        Pageable paging = PageRequest.of(page, pageSize);

        Page<News> pageNews = newsRepository.findAll(paging);
        long total = pageNews.getTotalElements();
        List<NewsResponse> newsDto = mapToNewsDto(pageNews.getContent());
        return new DataWithCounterDto(newsDto, total);
    }

    public NewsViewResponse get(Long id) {
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
