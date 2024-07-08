package org.example.itech_heaven.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SecurityHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final RequestCache requestCache;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> authoritied = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        if (user != null) {
            userService.updateLastLoginDate(user.getId());
        }
//        SavedRequest savedRequest = requestCache.getRequest(request, response);
//        String targetUrl = (savedRequest != null) ? savedRequest.getRedirectUrl() : null;
//        if (targetUrl != null && isValidUrl(targetUrl)) {
//            response.sendRedirect(targetUrl);
//            return;
//        }
        for (String auth : authoritied) {
            if (auth.equals("MANAGE_PERMISSION")|| auth.equals("ROLE_STAFF") || auth.equals("ROLE_ADMIN")) {
                response.sendRedirect("/dashboard");
                return;
            }
        }
        for (String auth : authoritied) {
            if (auth.equals("ROLE_CUSTOMER")) {
                response.sendRedirect("/home");
                return;
            }
        }
        response.sendRedirect("/");
    }

    private boolean isValidUrl(String url) {
        String[] validPages = {"/favicon.ico"};
        for (String page : validPages) {
            if (!url.contains(page)) {
                return true;
            }
        }
        return false;
    }
}
