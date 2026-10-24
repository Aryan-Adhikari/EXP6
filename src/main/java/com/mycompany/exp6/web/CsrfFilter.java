package com.mycompany.exp6.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;

@WebFilter("/*")
public class CsrfFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            HttpSession session = httpRequest.getSession(false);
            String expected = session == null ? null : (String) session.getAttribute(WebSecurity.CSRF_ATTRIBUTE);
            String submitted = httpRequest.getParameter(WebSecurity.CSRF_ATTRIBUTE);
            if (expected == null || submitted == null
                    || !MessageDigest.isEqual(expected.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                    submitted.getBytes(java.nio.charset.StandardCharsets.UTF_8))) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setHeader("Cache-Control", "no-store");
                if ("/login".equals(httpRequest.getServletPath())) {
                    if (session != null && session.getAttribute("userId") != null) {
                        httpResponse.sendRedirect(httpRequest.getContextPath() + "/employees");
                    } else {
                        httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?expired=1");
                    }
                    return;
                }
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                boolean signedIn = session != null && session.getAttribute("userId") != null;
                request.setAttribute("signedIn", signedIn);
                request.setAttribute("error", "This form expired or was already submitted. Review the page and try again.");
                request.getRequestDispatcher("/WEB-INF/views/csrf-error.jsp").forward(request, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
