package com.mycompany.exp6.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;

public final class WebSecurity {
    public static final String CSRF_ATTRIBUTE = "csrfToken";
    private static final SecureRandom RANDOM = new SecureRandom();

    private WebSecurity() {
    }

    public static String csrfToken(HttpSession session) {
        String token = (String) session.getAttribute(CSRF_ATTRIBUTE);
        if (token == null) {
            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute(CSRF_ATTRIBUTE, token);
        }
        return token;
    }

    public static void rotateCsrfToken(HttpSession session) {
        session.removeAttribute(CSRF_ATTRIBUTE);
        csrfToken(session);
    }

    public static String applicationUrl(HttpServletRequest request, String path) {
        return request.getContextPath() + path;
    }
}
