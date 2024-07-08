package org.example.itech_heaven.Service.Authentication;

import org.example.itech_heaven.Entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface Oauth2LoginStrategy {
    User addOauth2User(OAuth2User oAuth2User);
}
