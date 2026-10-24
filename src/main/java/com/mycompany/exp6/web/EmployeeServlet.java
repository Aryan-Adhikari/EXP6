package com.mycompany.exp6.web;

import com.mycompany.exp6.dao.EmployeeDAO;
import com.mycompany.exp6.model.Employee;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet("/employees/*")
public class EmployeeServlet extends HttpServlet {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        try {
            String path = request.getPathInfo();
            if (path == null || "/".equals(path)) {
                request.setAttribute("employees", employeeDAO.findAll(userId(request)));
                takeFlash(request);
                request.getRequestDispatcher("/WEB-INF/views/employees.jsp").forward(request, response);
            } else if ("/new".equals(path)) {
                showForm(request, response, null);
            } else if ("/edit".equals(path)) {
                Employee employee = employeeDAO.findById(id(request), userId(request));
                if (employee == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found.");
                    return;
                }
                showForm(request, response, employee);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid employee ID.");
        } catch (SQLException exception) {
            databaseError(request, response, exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        String path = request.getPathInfo();
        try {
            if ("/create".equals(path) || "/update".equals(path)) {
                save(request, response, "/update".equals(path));
            } else if ("/delete".equals(path)) {
                if (!employeeDAO.delete(id(request), userId(request))) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found.");
                    return;
                }
                setFlash(request, "Employee deleted.");
                response.sendRedirect(request.getContextPath() + "/employees");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid employee ID.");
        } catch (SQLException exception) {
            databaseError(request, response, exception);
        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response, boolean update)
            throws ServletException, IOException, SQLException {
        String name = value(request, "name");
        String email = value(request, "email");
        String department = value(request, "department");
        String salaryText = value(request, "salary");
        String error = null;
        BigDecimal salary = null;
        if (name.isBlank() || name.length() > 100) {
            error = "Name is required and must be at most 100 characters.";
        } else if (!EMAIL_PATTERN.matcher(email).matches() || email.length() > 190) {
            error = "Enter a valid email address (up to 190 characters).";
        } else if (department.isBlank() || department.length() > 100) {
            error = "Department is required and must be at most 100 characters.";
        } else {
            try {
                salary = new BigDecimal(salaryText);
                if (salary.signum() < 0 || salary.precision() > 12 || salary.scale() > 2) {
                    error = "Salary must be a non-negative amount with up to 2 decimal places.";
                }
            } catch (NumberFormatException exception) {
                error = "Enter a valid salary.";
            }
        }
        long employeeId = update ? id(request) : 0;
        Employee employee = new Employee(employeeId, name, email, department, salary);
        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("employee", employee);
            request.setAttribute("editing", update);
            request.setAttribute("formAction", update ? "/employees/update?id=" + employeeId : "/employees/create");
            WebSecurity.csrfToken(request.getSession());
            request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
            return;
        }
        if (update) {
            if (!employeeDAO.update(employee, userId(request))) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found.");
                return;
            }
        } else {
            employeeDAO.create(employee, userId(request));
        }
        setFlash(request, update ? "Employee updated." : "Employee added.");
        response.sendRedirect(request.getContextPath() + "/employees");
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, Employee employee)
            throws ServletException, IOException {
        request.setAttribute("employee", employee);
        request.setAttribute("editing", employee != null);
        request.setAttribute("formAction", employee == null
                ? "/employees/create" : "/employees/update?id=" + employee.getId());
        WebSecurity.csrfToken(request.getSession());
        request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
    }

    private void databaseError(HttpServletRequest request, HttpServletResponse response, SQLException exception)
            throws ServletException, IOException {
        getServletContext().log("Employee database operation failed", exception);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        request.setAttribute("error",
                "The employee request could not be completed. Check the database connection and try again.");
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }

    private static long userId(HttpServletRequest request) {
        return (Long) request.getSession(false).getAttribute("userId");
    }

    private static long id(HttpServletRequest request) {
        String id = request.getParameter("id");
        try {
            long parsed = Long.parseLong(id);
            if (parsed < 1) {
                throw new NumberFormatException();
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid employee ID.", exception);
        }
    }

    private static String value(HttpServletRequest request, String parameter) {
        String value = request.getParameter(parameter);
        return value == null ? "" : value.trim();
    }

    private static void setFlash(HttpServletRequest request, String message) {
        request.getSession().setAttribute("flash", message);
    }

    private static void takeFlash(HttpServletRequest request) {
        HttpSession session = request.getSession();
        request.setAttribute("flash", session.getAttribute("flash"));
        session.removeAttribute("flash");
    }
}
