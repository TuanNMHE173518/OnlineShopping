package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.Feedback;
import org.example.itech_heaven.Entity.ProductType;

import java.util.List;

public interface FeedbackService {
    Feedback getFeedbackById(int id);

    void saveFeedback(Feedback feedback);

    void deleteFeedback(int feedbackId);
    List<Feedback> getRecentFeedbacks(int count);
}