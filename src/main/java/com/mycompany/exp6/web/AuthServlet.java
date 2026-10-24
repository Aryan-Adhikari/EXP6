package com.mycompany.exp6.web;

import com.mycompany.exp6.dao.UserDAO;
import com.mycompany.exp6.dao.UserDAO.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(urlPatterns = {"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        String path = request.getServletPath();
        if ("/logout".equals(path)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        WebSecurity.csrfToken(request.getSession());
        request.getRequestDispatcher("/WEB-INF/views" + path + ".jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        switch (request.getServletPath()) {
            case "/register" -> register(request, response);
            case "/login" -> login(request, response);
            case "/logout" -> logout(request, response);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = value(request, "name");
        String email = value(request, "email").toLowerCase(Locale.ROOT);
        String password = request.getParameter("password");
        String error = null;
        if (name.isBlank() || name.length() > 100) {
            error = "Enter a name between 1 and 100 characters.";
        } else if (!EMAIL_PATTERN.matcher(email).matches() || email.length() > 190) {
            error = "Enter a valid email address (up to 190 characters).";
        } else if (password == null || password.length() < 8
                || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
            error = "Password must contain at least 8 characters and no more than 72 UTF-8 bytes.";
        }
        if (error != null) {
            forwardError(request, response, "/WEB-INF/views/register.jsp", error);
            return;
        }
        try {
            userDAO.create(name, email, BCrypt.hashpw(password, BCrypt.gensalt(12)));
            response.sendRedirect(request.getContextPath() + "/login?registered=1");
        } catch (SQLException exception) {
            getServletContext().log("Could not register account", exception);
            boolean duplicateEmail = exception.getErrorCode() == 1062;
            response.setStatus(duplicateEmail
                    ? HttpServletResponse.SC_CONFLICT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String message = duplicateEmail ? "An account with that email already exists."
                    : "Registration could not be completed. Check the database connection and try again.";
            forwardError(request, response, "/WEB-INF/views/register.jsp", message);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = value(request, "email").toLowerCase(Locale.ROOT);
        String password = request.getParameter("password");
        if (email.isBlank() || password == null || password.isEmpty()) {
            forwardError(request, response, "/WEB-INF/views/login.jsp", "Enter your email and password.");
            return;
        }
        try {
            User user = userDAO.findByEmail(email);
            if (user == null || !BCrypt.checkpw(password, user.passwordHash())) {
                forwardError(request, response, "/WEB-INF/views/login.jsp", "Email or password is incorrect.");
                return;
            }
            HttpSession session = request.getSession();
            request.changeSessionId();
            session.setAttribute("userId", user.id());
            session.setAttribute("userName", user.name());
            WebSecurity.rotateCsrfToken(session);
            response.sendRedirect(request.getContextPath() + "/employees");
        } catch (SQLException exception) {
            getServletContext().log("Could not sign in", exception);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            forwardError(request, response, "/WEB-INF/views/login.jsp",
                    "Sign in could not be completed. Check the database connection and try again.");
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private static String value(HttpServletRequest request, String parameter) {
        String value = request.getParameter(parameter);
        return value == null ? "" : value.trim();
    }

    private void forwardError(HttpServletRequest request, HttpServletResponse response,
            String page, String error) throws ServletException, IOException {
        request.setAttribute("error", error);
        WebSecurity.csrfToken(request.getSession());
        request.getRequestDispatcher(page).forward(request, response);
    }
}
