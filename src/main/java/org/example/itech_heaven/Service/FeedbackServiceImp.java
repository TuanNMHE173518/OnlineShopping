package org.example.itech_heaven.Service;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.Feedback;
import org.example.itech_heaven.Entity.ProductType;
import org.example.itech_heaven.Repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImp implements FeedbackService {

    private final FeedbackRepository feedbackRepository;


    @Override
    public Feedback getFeedbackById(int id) {
        return feedbackRepository.getFeedbackById(id);
    }

    @Override
    public void saveFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
    }

    @Override
    public void deleteFeedback(int feedbackId) {
        feedbackRepository.deleteById(feedbackId);
    }

    @Override
    public List<Feedback> getRecentFeedbacks(int count) {
        return feedbackRepository.findTop20ByOrderByDateDesc();
    }

}
