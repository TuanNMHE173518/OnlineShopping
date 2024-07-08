package org.example.itech_heaven.Service;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.News;
import org.example.itech_heaven.Repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImp implements NewsService{
    private final NewsRepository newsRepository;


    @Override
    public void saveNews(News news) {
        newsRepository.save(news);
    }


    @Override
    public News findById(int id) {
        return newsRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteNews(int id) {
        newsRepository.deleteById(id);
    }

    @Override
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }
}
