package com.example.livealone;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

// public class MockSpringSecurityFilter implements Filter {
//     @Override
//     public void init(FilterConfig filterConfig) {
//     }
//
//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//         SecurityContextHolder.getContext()
//                 .setAuthentication((Authentication) ((HttpServletRequest) request).getUserPrincipal());
//
//         filterChain.doFilter(request, response);
//     }
//
//     @Override
//     public void destroy() {
//         SecurityContextHolder.clearContext();
//     }
// }