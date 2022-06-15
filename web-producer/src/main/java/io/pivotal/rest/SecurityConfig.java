package io.pivotal.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@EnableWebSecurity
//@EnableConfigurationProperties(UserCacheProperties.class)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ALL_URL_PATTERN = "/**";

//    private final TechnicalEndpointService technicalEndpointService;
//    private final CsrfProperties csrfProperties;
//    private final Customizer<CsrfConfigurer<HttpSecurity>> csrfCustomizer;
//    private final Customizer<HeadersConfigurer<HttpSecurity>> headersCustomizer;
//    private final AuthenticationService authenticationService;
//    private final UserCacheProperties userCacheProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        configureCsrf(http);
//        ofNullable(technicalEndpointService).ifPresent(service -> service.allowTechnicalEndpoints(http));
        http
//                .headers(headersCustomizer)
                .cors()
                .and()
                .authorizeRequests()
                // Allow CORS option calls
                .antMatchers(OPTIONS, ALL_URL_PATTERN).permitAll()
                .antMatchers("/swagger-ui/**", "/v3/**").permitAll()
//                .antMatchers(GET, ENVIRONMENT_CONFIG_SUFFIX).permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer().jwt();
    }

    private void configureCsrf(HttpSecurity http) throws Exception {
//        if (toBoolean(csrfProperties.getCsrfEnabled())) {
//            http.csrf(csrfCustomizer);
//        } else {
            http.csrf().disable();
//        }
    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        authenticationService.initUsers(auth, userCacheProperties.getUsers());
//    }
}
