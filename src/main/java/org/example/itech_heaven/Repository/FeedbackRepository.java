package org.example.itech_heaven.Repository;

import org.example.itech_heaven.Entity.Feedback;
import org.example.itech_heaven.Entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Feedback getFeedbackById(int id);
    List<Feedback> findTop20ByOrderByDateDesc();
}
