package com.lautamgioi.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class WebUtil {
    private WebUtil() {
    }

    public static Integer intParam(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String context(HttpServletRequest request, String path) {
        return request.getContextPath() + path;
    }

    public static void flash(HttpServletRequest request, String message) {
        request.getSession().setAttribute("flash", message);
    }

    public static String consumeFlash(HttpSession session) {
        Object value = session.getAttribute("flash");
        session.removeAttribute("flash");
        return value == null ? null : value.toString();
    }
}
