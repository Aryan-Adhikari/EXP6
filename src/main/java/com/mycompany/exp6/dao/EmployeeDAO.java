package com.mycompany.exp6.dao;

import com.mycompany.exp6.db.Database;
import com.mycompany.exp6.model.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    public List<Employee> findAll(long userId) throws SQLException {
        String sql = "SELECT id, name, email, department, salary FROM employees WHERE user_id = ? ORDER BY id DESC";
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    employees.add(readEmployee(result));
                }
            }
        }
        return employees;
    }

    public Employee findById(long id, long userId) throws SQLException {
        String sql = "SELECT id, name, email, department, salary FROM employees WHERE id = ? AND user_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.setLong(2, userId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? readEmployee(result) : null;
            }
        }
    }

    public void create(Employee employee, long userId) throws SQLException {
        String sql = "INSERT INTO employees (user_id, name, email, department, salary) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, userId);
            bindEmployee(statement, employee, 2);
            statement.executeUpdate();
        }
    }

    public boolean update(Employee employee, long userId) throws SQLException {
        String sql = "UPDATE employees SET name = ?, email = ?, department = ?, salary = ? WHERE id = ? AND user_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bindEmployee(statement, employee, 1);
            statement.setLong(5, employee.getId());
            statement.setLong(6, userId);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean delete(long id, long userId) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ? AND user_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.setLong(2, userId);
            return statement.executeUpdate() == 1;
        }
    }

    private static void bindEmployee(PreparedStatement statement, Employee employee, int start) throws SQLException {
        statement.setString(start, employee.getName());
        statement.setString(start + 1, employee.getEmail());
        statement.setString(start + 2, employee.getDepartment());
        statement.setBigDecimal(start + 3, employee.getSalary());
    }

    private static Employee readEmployee(ResultSet result) throws SQLException {
        return new Employee(result.getLong("id"), result.getString("name"), result.getString("email"),
                result.getString("department"), result.getBigDecimal("salary"));
    }
}
