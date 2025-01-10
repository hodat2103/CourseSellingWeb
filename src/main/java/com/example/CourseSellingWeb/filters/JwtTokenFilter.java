package com.example.CourseSellingWeb.filters;

import com.example.CourseSellingWeb.components.JwtTokenUtils;
import com.example.CourseSellingWeb.models.Account;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
//            if (isBypassToken(request)) {
//                logger.debug("Bypassing token for request: {}", request.getRequestURI());
//                filterChain.doFilter(request, response);
//                return;
//
//            }
            if (isBypassToken(request)) {
                logger.debug("Bypassing token for request: {}", request.getRequestURI());
                if (!response.isCommitted()) {
                    filterChain.doFilter(request, response);
                }
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.debug("Missing or invalid Authorization header for request: {}", request.getRequestURI());
                if (!response.isCommitted()) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                }
                return;
            }

            final String token = authHeader.substring(7);
            final String phoneNumber = jwtTokenUtils.extractUsername(token);
            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Account userDetails = (Account) userDetailsService.loadUserByUsername(phoneNumber);
                if (jwtTokenUtils.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.debug("Authenticated user: {}", phoneNumber);
                } else {
                    logger.debug("Invalid JWT token for user: {}", phoneNumber);
                }
            }
            filterChain.doFilter(request, response);
            logger.debug("After filterChain.doFilter");
        } catch (Exception e) {
            logger.error("Error during JWT token validation", e);
            if (!response.isCommitted()) {

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }
        }
    }

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/course_videos/uploadVideo**", apiPrefix), "POST"),

                Pair.of(String.format("%s/fields", apiPrefix), "GET"),
                Pair.of(String.format("%s/fields**", apiPrefix), "GET"),
                Pair.of(String.format("%s/mentors/all",apiPrefix),"GET"),
                Pair.of(String.format("%s/mentors/",apiPrefix),"GET"),

                Pair.of(String.format("%s/courses/discounts", apiPrefix), "GET"),
                Pair.of(String.format("%s/image_detail/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/courses", apiPrefix), "GET"),
                Pair.of(String.format("%s/excel/export/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/roles", apiPrefix), "GET"),
                Pair.of(String.format("%s/accounts/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/accounts/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/health_check", apiPrefix), "GET"),
                Pair.of(String.format("%s/courses/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/courses/videos**", apiPrefix), "GET"),
                Pair.of(String.format("%s/employees**", apiPrefix), "GET"),
                Pair.of(String.format("%s/mentors**", apiPrefix), "GET"),
                Pair.of(String.format("%s/blogs", apiPrefix), "GET"),
                Pair.of(String.format("%s/blogs/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/sliders", apiPrefix), "GET"),
                Pair.of(String.format("%s/languages", apiPrefix), "GET"),
                Pair.of(String.format("%s/languages/**", apiPrefix), "GET"),

                Pair.of(String.format("%s/coupons", apiPrefix), "GET"),
                Pair.of(String.format("%s/coupon_conditions**", apiPrefix), "GET")
        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

//        logger.debug("Request Path: {}", requestPath);
//        logger.debug("Request Method: {}", requestMethod);

        for (Pair<String, String> bypassToken : bypassTokens) {
//            logger.debug("Checking bypassToken: {} {}", bypassToken.getFirst(), bypassToken.getSecond());
            if (requestPath.contains(bypassToken.getFirst()) && requestMethod.equals(bypassToken.getSecond())) {
//                logger.debug("Bypass token matched for request: {}", requestPath);
                return true;
            }
        }
//        logger.debug("No bypass token matched for request: {}", requestPath);
        return false;
    }
}
