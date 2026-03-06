package com.projekt.Spring_Boot_API.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projekt.Spring_Boot_API.models.User;
import com.projekt.Spring_Boot_API.services.UserService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;
    private final WebClient webClient = WebClient
            .builder()
            .baseUrl("https://api.github.com")
            .build();

    @Override
    public void onAuthenticationSuccess(
            @NonNull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Authentication authentication
    ) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        String oidcProvider = token.getAuthorizedClientRegistrationId();
        String oidcId = token.getName();

        Optional<User> user = userService.getUserByOidcId(oidcId, oidcProvider);

        if (user.isEmpty()) {
            User createdUser = createUser(oidcId, oidcProvider, request, authentication);
            if (createdUser == null) {
                response.getWriter().println("Failed to create account. Try again!");
            } else {
                response.getWriter().println("Successfully created account.");
            }
        } else {
            response.getWriter().println("Logged in as " + user.get().getUsername() + ".");
        }
    }

    @Nullable
    private User createUser(String oidcId, String oidcProvider, HttpServletRequest request, Authentication authentication) {
        OAuth2AuthorizedClient client = authorizedClientRepository.loadAuthorizedClient(oidcProvider, authentication, request);
        String accessToken = client.getAccessToken().getTokenValue();

        var emailResponse = webClient
                .get()
                .uri("/user/emails")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<GetEmailsResponse>>() {
                })
                .block();

        if (emailResponse == null) {
            return null;
        }

        if (emailResponse.getStatusCode() != HttpStatus.OK) {
            return null;
        }

        var emails = emailResponse.getBody();
        if (emails == null || emails.isEmpty()) {
            return null;
        }

        var email = emails.getFirst();

        return userService.registerOidcUser(email.getEmail(), oidcId, oidcProvider);
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GetEmailsResponse {
        private String email;
    }
}
