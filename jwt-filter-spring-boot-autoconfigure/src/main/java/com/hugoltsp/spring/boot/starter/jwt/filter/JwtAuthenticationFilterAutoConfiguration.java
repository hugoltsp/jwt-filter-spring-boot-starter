package com.hugoltsp.spring.boot.starter.jwt.filter;

import com.hugoltsp.spring.boot.starter.jwt.filter.authentication.AuthenticationContextFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.request.HttpRequestMatcher;
import com.hugoltsp.spring.boot.starter.jwt.filter.token.JwtParser;
import com.hugoltsp.spring.boot.starter.jwt.filter.token.JwtValidator;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetails;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsFactory;
import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetailsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnClass(JwtAuthenticationFilter.class)
@EnableConfigurationProperties(JwtAuthenticationSettings.class)
public class JwtAuthenticationFilterAutoConfiguration {

    private static final String LOG_NO_CUSTOM_BEAN_PROVIDED = "No bean of type [{}] provided, falling back to default implementation.";

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilterAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public JwtParser jwtParser(JwtAuthenticationSettings settings) {
        LOGGER.info(LOG_NO_CUSTOM_BEAN_PROVIDED, JwtParser.class.getSimpleName());
        return new DefaultJwtParser(settings.getSecretKey());
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtValidator noOpJwtValidator() {
        LOGGER.info(LOG_NO_CUSTOM_BEAN_PROVIDED, JwtValidator.class.getSimpleName());
        return jwt -> {
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpRequestMatcher requestMatcher(JwtAuthenticationSettings settings) {
        LOGGER.info(LOG_NO_CUSTOM_BEAN_PROVIDED, HttpRequestMatcher.class.getSimpleName());
        return new DefaultHttpRequestMatcher(settings.getPublicResources()
                .stream()
                .filter(Objects::nonNull)
                .map(PublicResourceWrapper::new)
                .collect(Collectors.toList()));
    }

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsValidator<UserDetails> noOpUserDetailsValidator() {
        LOGGER.info(LOG_NO_CUSTOM_BEAN_PROVIDED, UserDetailsValidator.class.getSimpleName());
        return userDetails -> {
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsFactory<UserDetails> noOpUserDetailsFactory() {
        LOGGER.info(LOG_NO_CUSTOM_BEAN_PROVIDED, UserDetailsFactory.class.getSimpleName());
        return claims -> Optional.of((UserDetails) () -> claims);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationContextFactory<UserDetails> simpleAuthenticationContextFactory() {
        LOGGER.info(LOG_NO_CUSTOM_BEAN_PROVIDED, AuthenticationContextFactory.class.getSimpleName());
        return AuthenticationContext::new;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(HttpRequestMatcher httpRequestMatcher,
                                                           JwtParser jwtParser,
                                                           JwtValidator jwtValidator,
                                                           UserDetailsValidator<UserDetails> userDetailsValidator,
                                                           UserDetailsFactory<UserDetails> userDetailsFactory,
                                                           AuthenticationContextFactory<UserDetails> authenticationContextFactory) {

        return new JwtAuthenticationFilter(httpRequestMatcher,
                jwtValidator,
                jwtParser,
                userDetailsValidator,
                userDetailsFactory,
                authenticationContextFactory);
    }

}
