//package com.graduatepj.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration // 안에 bean 설정 가능
//@EnableWebSecurity // 스프링 security 지원 가능하게 함
//public class WebSecurityConfig  {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception {
//        http
//                .cors().disable()
//                .csrf().disable() // CSRF 공격에 대한 방어 해제
//                .authorizeRequests() // URI에 따른 페이지에 대한 권한 부여하기 위해 시작하는 메소드
//                    .antMatchers("/user/**").authenticated() // 접근시 인가가 필요한 URI 설정 가능, authenticated() 해당 URI는 인증이 필요한 RUI
//                    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//                    .anyRequest().permitAll() // 특정 URI를 제외한 나머지 URI는 전부 인가
//                    .and()
//                .formLogin() // 로그인 형태를 지원하는 spring security 기능
////                    .disable()
//                    .loginPage("/login")// 로그인 페이지 설정, 로그인 없이 로그인이 필요한 페이지를 들어갔을 때 로그인 페이지로 redirect하기 위한 메서드
//                    .failureUrl("/login")
//                    .loginProcessingUrl("/member/signup") // formLogin의 자동 로그인 방식 이용
//                    .defaultSuccessUrl("/"); // 로그인 성공했을 때 기본 URL
//
//        // 이거 하면 처음에 뜨는 로그인 페이지 안뜨게 됨
//        // .antMatchers("/images/**").permitAll() //image 폴더를 login 없이 허용
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder getpasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//
//}
