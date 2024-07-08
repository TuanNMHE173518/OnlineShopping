package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.News;

import java.util.List;

public interface NewsService {
    void saveNews(News news);

    News findById(int id);
    void deleteNews(int id);
    List<News> getAllNews();

}
