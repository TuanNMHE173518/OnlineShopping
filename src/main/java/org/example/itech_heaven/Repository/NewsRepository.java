package org.example.itech_heaven.Repository;

import org.example.itech_heaven.Entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Integer> {
}
