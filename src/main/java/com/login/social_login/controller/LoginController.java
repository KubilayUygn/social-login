package com.login.social_login.controller;

import com.login.social_login.service.SocialLoginService;
import com.login.social_login.service.TwitterOAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private TwitterOAuthService twitterOAuthService;
    @Autowired
    private SocialLoginService socialLoginService;

    @GetMapping("/oauth_login")
    public String getLoginPage(Model model) throws Exception {
        Map<String, String> oauth2AuthenticationUrls = socialLoginService.getSocialLoginUrls();
        model.addAttribute("urls", oauth2AuthenticationUrls);
        return "oauth_login";
    }

    @GetMapping("/loginSuccess")
    public String successUrl() {
        return "success";
    }

    @GetMapping("/facebook/loginSuccess")
    public String facebookCallback() {
        return "facebook_login_success";
    }

    @GetMapping("/twitter/loginSuccess")
    public String twitterCallback(@RequestParam("oauth_token") String oauthToken,
                                  @RequestParam("oauth_verifier") String oauthVerifier, HttpSession session) throws Exception {
        twitterOAuthService.authenticateUserWithTwitter(oauthToken, oauthVerifier, session);
        return "twitter_login_success";
    }

    @GetMapping("/loginFailure")
    public ResponseEntity<String> failureUrl() {
        return ResponseEntity.ok("Invalid username or password");
    }
}
