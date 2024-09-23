package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.request.NewsCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.entity.*;
import com.thanhdat.quanlyhoctap.exception.code.ErrorCode;
import com.thanhdat.quanlyhoctap.exception.type.AppException;
import com.thanhdat.quanlyhoctap.mapper.NewsMapper;
import com.thanhdat.quanlyhoctap.repository.NewsRepository;
import com.thanhdat.quanlyhoctap.repository.StaffRepository;
import com.thanhdat.quanlyhoctap.service.NewsService;
import com.thanhdat.quanlyhoctap.util.PagingHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
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

    PagingHelper pagingHelper;

    NewsMapper newsMapper;

    @Override
    public void create(NewsCrudRequest createRequest) {
        Staff author = staffRepository.findById(createRequest.getAuthorId())
                .orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND));

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
        if (!newsRepository.existsById(id)) {
            throw newsNotFound();
        }

        newsRepository.deleteById(id);
    }

    @Override
    public NewsViewCrudResponse getById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(this::newsNotFound);
        return newsMapper.toNewsViewCrudResponse(news);
    }

    @Override
    public void update(Long id, NewsCrudRequest updateRequest) {
        News news = newsRepository.findById(id)
                .orElseThrow(this::newsNotFound);

        news.setTitle(updateRequest.getTitle());
        news.setContent(updateRequest.getContent());
        news.setIsImportant(updateRequest.getIsImportant());

        Boolean isAuthorChanged = !news.getAuthor().getId().equals(updateRequest.getAuthorId());
        if (isAuthorChanged) {
            Staff author = staffRepository.findById(updateRequest.getAuthorId())
                    .orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND));
            news.setAuthor(author);
        }

        newsRepository.save(news);
    }

    @Override
    public DataWithCounterDto<NewsCrudResponse> getAllCrud(Map<String, String> params) {
        Pageable paging = pagingHelper.getPageable(params);
        Page<News> page = newsRepository.findAll(paging);
        List<NewsCrudResponse> dto = page.getContent().stream()
                .map(newsMapper::toNewsCrudResponse)
                .collect(Collectors.toList());
        long total = page.getTotalElements();
        return new DataWithCounterDto<>(dto, total);
    }

    public DataWithCounterDto<NewsResponse> getAll(Map<String, String> params) {
        Pageable paging = pagingHelper.getPageable(params);

        Page<News> pageNews = newsRepository.findAll(paging);
        long total = pageNews.getTotalElements();
        List<NewsResponse> newsDto = pageNews.getContent().stream()
                .map(newsMapper::toNewsResponse)
                .collect(Collectors.toList());
        return new DataWithCounterDto(newsDto, total);
    }

    public NewsViewResponse get(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(this::newsNotFound);
        return newsMapper.toNewsViewResponse(news);
    }

    private AppException newsNotFound() {
        return new AppException(ErrorCode.NEWS_NOT_FOUND);
    }
}
