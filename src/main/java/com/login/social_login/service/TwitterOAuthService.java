package com.login.social_login.service;

import com.login.social_login.model.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TwitterOAuthService {

    @Value("${twitter.consumer.key}")
    private String CONSUMER_KEY;
    @Value("${twitter.consumer.secret}")
    private String CONSUMER_SECRET;
    @Value("${twitter.callback.url}")
    private String CALLBACK_URL;

    public String getTwitterAuthorizationUrl() throws Exception {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(CONSUMER_KEY);
        cb.setOAuthConsumerSecret(CONSUMER_SECRET);

        Configuration configuration = cb.build();

        TwitterFactory tf = new TwitterFactory(configuration);
        Twitter twitter = tf.getInstance();

        RequestToken requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);

        return requestToken.getAuthorizationURL();
    }

    public void authenticateUserWithTwitter(String oauthToken, String oauthVerifier, HttpSession session) throws Exception {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(CONSUMER_KEY);
        cb.setOAuthConsumerSecret(CONSUMER_SECRET);

        Configuration configuration = cb.build();

        TwitterFactory tf = new TwitterFactory(configuration);
        Twitter twitter = tf.getInstance();

        RequestToken requestToken = new RequestToken(oauthToken, "");
        AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);

        User user = twitter.verifyCredentials();
        UserDetails userDetails = new CustomUserDetails(user.getName(), String.valueOf(user.getId()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }
}
