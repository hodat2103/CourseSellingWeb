package com.example.CourseSellingWeb.configurations;

import com.example.CourseSellingWeb.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import java.util.Arrays;
import java.util.List;

@Configuration
//@EnableMethodSecurity
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
@RequiredArgsConstructor

public class WebSecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final JwtTokenFilter jwtTokenFilter;




    private final List<Pair<String, HttpMethod>> noByPassTokenAdmin = List.of(

            Pair.of(String.format("%s/employees/**", apiPrefix), HttpMethod.DELETE),

            Pair.of(String.format("%s/accounts", apiPrefix), HttpMethod.GET),
            Pair.of(String.format("%s/accounts/block/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/accounts/unblock/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/accounts/**", apiPrefix), HttpMethod.DELETE),
            Pair.of(String.format("%s/accounts/**", apiPrefix), HttpMethod.PUT),

            Pair.of(String.format("%s/roles/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/roles/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/roles/**", apiPrefix), HttpMethod.DELETE)


            );
    private final List<Pair<String, HttpMethod>> noByPassTokenSalesPerson = List.of(
            Pair.of(String.format("%s/orders/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/orders/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/orders/**", apiPrefix), HttpMethod.DELETE),
            Pair.of(String.format("%s/orders/**", apiPrefix), HttpMethod.POST),

            Pair.of(String.format("%s/payments/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/payments/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/payments/**", apiPrefix), HttpMethod.DELETE),
            Pair.of(String.format("%s/payments/**", apiPrefix), HttpMethod.POST),

            Pair.of(String.format("%s/statistics/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/statistics/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/statistics/**", apiPrefix), HttpMethod.DELETE),
            Pair.of(String.format("%s/statistics/**", apiPrefix), HttpMethod.POST),

            Pair.of(String.format("%s/employees/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/employees/**", apiPrefix), HttpMethod.PUT),

            Pair.of(String.format("%s/excel/export/**", apiPrefix), HttpMethod.GET),

            Pair.of(String.format("%s/pdf/export", apiPrefix), HttpMethod.GET)


    );
    private final List<Pair<String, HttpMethod>> noByPassTokenTechnical = List.of(
            Pair.of(String.format("%s/courses/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/courses/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/courses/**", apiPrefix), HttpMethod.DELETE),

            Pair.of(String.format("%s/employees/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/employees/**", apiPrefix), HttpMethod.PUT),

            Pair.of(String.format("%s/mentors/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/mentors/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/mentors/**", apiPrefix), HttpMethod.DELETE),

            Pair.of(String.format("%s/coupons/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/coupons/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/coupons/**", apiPrefix), HttpMethod.DELETE),
//            Pair.of(String.format("%s/coupons/search/**", apiPrefix), HttpMethod.GET),


            Pair.of(String.format("%s/coupon_conditions/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/coupon_conditions/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/coupon_conditions/**", apiPrefix), HttpMethod.DELETE),

            Pair.of(String.format("%s/fields/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/fields/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/fields/**", apiPrefix), HttpMethod.DELETE),

            Pair.of(String.format("%s/languages/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/languages/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/languages/**", apiPrefix), HttpMethod.DELETE),

            Pair.of(String.format("%s/blogs/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/blogs/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/blogs/**", apiPrefix), HttpMethod.DELETE),

            Pair.of(String.format("%s/sliders/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/sliders/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/sliders/**", apiPrefix), HttpMethod.DELETE),

            Pair.of(String.format("%s/course_videos/uploadVideo**", apiPrefix), HttpMethod.POST)


    );

    private final List<Pair<String, HttpMethod>> noByPassTokenUser = List.of(
            Pair.of(String.format("%s/users/reset-password/**", apiPrefix), HttpMethod.PUT),
            Pair.of(String.format("%s/order/**", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/user_course", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/user_course/**", apiPrefix), HttpMethod.GET)



    );
    private final List<Pair<String, HttpMethod>> bypassTokens = List.of(
            Pair.of(String.format("%s/image_detail/**", apiPrefix), HttpMethod.GET),
            Pair.of(String.format("%s/courses/discounts", apiPrefix), HttpMethod.GET),
            Pair.of(String.format("%s/roles", apiPrefix), HttpMethod.GET),
            Pair.of(String.format("%s/accounts/register", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/accounts/login", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/health_check", apiPrefix), HttpMethod.GET),
            Pair.of(String.format("%s/courses/**", apiPrefix), HttpMethod.GET),
            Pair.of(String.format("%s/courses/videos/**", apiPrefix), HttpMethod.GET),
            Pair.of(String.format("%s/course_videos/uploadVideo", apiPrefix), HttpMethod.POST),
            Pair.of(String.format("%s/employees/**", apiPrefix), HttpMethod.GET),
            Pair.of(String.format("%s/mentors/**", apiPrefix), HttpMethod.GET),
            Pair.of(String.format("%s/blogs", apiPrefix), HttpMethod.GET),
            Pair.of(String.format("%s/sliders", apiPrefix), HttpMethod.GET),
            Pair.of(String.format("%s/coupons", apiPrefix), HttpMethod.GET),
            Pair.of(String.format("%s/coupon_conditions/**", apiPrefix), HttpMethod.GET)
    );


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(requests -> {
                    requests.requestMatchers(
                            String.format("%s/accounts/register", apiPrefix),
                            String.format("%s/accounts/login", apiPrefix),
                            String.format("%s/accounts/details", apiPrefix)
                    ).permitAll();
                    requests.requestMatchers(
                            String.format("%s/courses/**",apiPrefix),
                            String.format("%s/courses",apiPrefix),
                            String.format("%s/image_detail/**",apiPrefix),
                            String.format("%s/languages",apiPrefix),
                            String.format("%s/languages/**",apiPrefix),
                            String.format("%s/fields",apiPrefix),
                            String.format("%s/fields/**",apiPrefix),
                            String.format("%s/mentors/all",apiPrefix),
                            String.format("%s/mentors/**",apiPrefix),
                            String.format("%s/blogs",apiPrefix),
                            String.format("%s/blogs/**",apiPrefix)



                    ).permitAll();
                    //Public access (permit all)
                    bypassTokens.forEach(api ->
                            requests.requestMatchers(api.getSecond(), api.getFirst()).permitAll());
                    // Admin role
                    noByPassTokenAdmin.forEach(api ->
                            requests.requestMatchers(api.getSecond(), api.getFirst()).hasRole("ADMIN")
                    );
                    // Sales person role
                    noByPassTokenSalesPerson.forEach(api ->
                            requests.requestMatchers(api.getSecond(), api.getFirst()).hasRole("SALES_PERSON")
                    );
                    // Technical Staff role
                    noByPassTokenTechnical.forEach(api ->
                            requests.requestMatchers(api.getSecond(), api.getFirst()).hasRole("TECHNICAL_STAFF")
                    );

                    // user must login
                    noByPassTokenUser.forEach(api ->
                            requests.requestMatchers(api.getSecond(), api.getFirst()).hasRole("USER")
                    );

                    requests.anyRequest().authenticated();
                });

        // CORS configuration
        http.cors(cors -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("*")); // Specify your frontend URL here
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
            configuration.setExposedHeaders(List.of("x-auth-token"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            cors.configurationSource(source);
        });

        return http.build();
    }

    }