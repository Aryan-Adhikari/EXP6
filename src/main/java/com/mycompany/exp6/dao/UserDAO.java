package com.mycompany.exp6.dao;

import com.mycompany.exp6.db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    public long create(String name, String email, String passwordHash) throws SQLException {
        String sql = "INSERT INTO users (name, email, password_hash) VALUES (?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, passwordHash);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
                throw new SQLException("User creation did not return an ID.");
            }
        }
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT id, name, email, password_hash FROM users WHERE email = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new User(result.getLong("id"), result.getString("name"),
                            result.getString("email"), result.getString("password_hash"));
                }
                return null;
            }
        }
    }

    public record User(long id, String name, String email, String passwordHash) {
    }
}
