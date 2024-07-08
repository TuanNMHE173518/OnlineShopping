package org.example.itech_heaven.Controller.Authentication;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Service.Authentication.Oauth2LoginStrategy;
import org.example.itech_heaven.Service.Authentication.Oauth2LoginStrategyFactory;
import org.example.itech_heaven.Service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Transactional
@Controller
@RequiredArgsConstructor
@RequestMapping("/login-oauth2")
public class Oauth2Controller {

    private final Oauth2LoginStrategyFactory oauth2LoginStrategyFactory;
    private final UserService userService;

    @GetMapping
    public String processOauth2Login(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof OAuth2AuthenticationToken)){
            return "redirect:/login?error";
        }
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

        Oauth2LoginStrategy loginStrategy = oauth2LoginStrategyFactory.getStrategy(provider);


        String email = oAuth2User.getAttribute("email");
        User user = userService.findByEmail(email);

        if (user == null){
            user = loginStrategy.addOauth2User(oAuth2User);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return "redirect:/home";
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("MANAGE_PERMISSION")) || user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STAFF")) || user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/indexSA";
        }
        return "redirect:/home";
    }
}
