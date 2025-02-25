package com.login.social_login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SocialLoginService {
    private static String authorizationRequestBaseUri = "oauth2/authorization";

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private TwitterOAuthService twitterOAuthService;

    public Map<String, String> getSocialLoginUrls() throws Exception {
        Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration -> oauth2AuthenticationUrls
                .put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()));

        String twitterAuthUrl = twitterOAuthService.getTwitterAuthorizationUrl();
        oauth2AuthenticationUrls.put("Twitter", twitterAuthUrl);

        return oauth2AuthenticationUrls;
    }
}
