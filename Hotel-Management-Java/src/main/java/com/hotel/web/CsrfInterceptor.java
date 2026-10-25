package com.hotel.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Gives every session a random token (available in JSP as ${sessionScope.csrfToken}) and rejects
 * any POST that doesn't send it back in the "_csrf" field.
 */
public class CsrfInterceptor implements HandlerInterceptor {

    public static final String SESSION_KEY = "csrfToken";
    public static final String FIELD_NAME = "_csrf";

    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute(SESSION_KEY);
        if (token == null) {
            byte[] bytes = new byte[24];
            RANDOM.nextBytes(bytes);
            token = HexFormat.of().formatHex(bytes);
            session.setAttribute(SESSION_KEY, token);
        }

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String sent = request.getParameter(FIELD_NAME);
            boolean ok = sent != null && MessageDigest.isEqual(
                    token.getBytes(StandardCharsets.UTF_8), sent.getBytes(StandardCharsets.UTF_8));
            if (!ok) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid form token");
                return false;
            }
        }
        return true;
    }
}
