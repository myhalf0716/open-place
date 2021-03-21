package kr.ggang.openplaces.conf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import kr.ggang.openplaces.security.JwtAuthenticationFilter;
import kr.ggang.openplaces.security.JwtUtil;
import kr.ggang.openplaces.security.RestAccessDeniedHandler;
import kr.ggang.openplaces.security.RestAuthenticationEntryPoint;
import kr.ggang.openplaces.security.RestAuthenticationFailureHandler;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/account"),
            new AntPathRequestMatcher("/account/**")
            );

    private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

    private final JwtUtil jwtTokenUtil;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PUBLIC_URLS);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http
        .httpBasic().disable()
        .csrf().disable() // rest api이므로 csrf 보안이 필요없음.
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token 인증이므로 session 없
        .and()
            .authorizeRequests() //authority checking
                .antMatchers("/account/**", "/h2-console/**").permitAll() // 가입 및 인증 주소는 누구나 접근가능
                .anyRequest().hasRole("MEMBER") // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
        .and()
            .exceptionHandling().accessDeniedHandler(restAccessDeniedHandler())
        .and()
            .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
        .and()  
                // Jwt Token filter를 filter chain에 삽입. 
                // JWT 로부터 Authenticate 객체를 생성하여 Context에 저장(다음 filter 가 Authentication 객체로 인증 수행 ) 
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class);
    }
    
    AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    AuthenticationFailureHandler failureHandler() {
        final AuthenticationFailureHandler failureHandler =
                new RestAuthenticationFailureHandler();
        return failureHandler;
    }

    @Bean
    AccessDeniedHandler restAccessDeniedHandler() {
        
        final AccessDeniedHandler restAccessDeniedHandler = 
                 new RestAccessDeniedHandler();
        return restAccessDeniedHandler;
        
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("PasswordEncoderFactories.createDelegatingPasswordEncoder() : [{}]", passwordEncoder.getClass().getName());
        return passwordEncoder;
    }

}
