package org.example.itech_heaven.Service.Authentication;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Oauth2LoginStrategyFactory {
    private final GoogleLoginStrategy googleLoginStrategy;
    private final FacebookLoginStrategy facebookLoginStrategy;
    private Map<String, Oauth2LoginStrategy> strategies;

    @PostConstruct
    public void init() {
        strategies = new HashMap<>();
        strategies.put("google", googleLoginStrategy);
        strategies.put("facebook", facebookLoginStrategy);
    }

    public Oauth2LoginStrategy getStrategy(String provider) {
        return strategies.get(provider);
    }
}
