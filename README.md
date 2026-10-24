# PeopleDesk — JDBC Employee Management

A Java 17, Jakarta Servlet/JSP, JDBC, and MySQL web application. Users can register, sign in, and manage their own employee records.

## Requirements

- Java 17 or newer
- Maven
- MySQL 8+
- Apache Tomcat 11

## Database setup

Run `database/schema.sql` in MySQL Workbench. The application defaults to a local MySQL server at `localhost:3306`, database `employee_management`, user `root`, and an empty password.

Override the connection settings with environment variables before starting Tomcat:

- `DB_URL` — JDBC URL (optional; defaults to `jdbc:mysql://localhost:3306/employee_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`)
- `DB_USER` — MySQL user (optional; defaults to `root`)
- `DB_PASSWORD` — MySQL password (optional; defaults to empty)

## Build and run

From the project folder:

```text
mvn clean package
```

Deploy `target/EXP6.war` to Tomcat 11's `webapps` folder, start Tomcat, and open `http://localhost:8080/EXP6/`. Create an account, sign in, and use the employee directory to add, view, edit, or delete records.

Passwords are stored as BCrypt hashes. SQL uses prepared statements, employee rows are scoped to the signed-in account, form submissions are protected with CSRF tokens, and server-side validation is applied in addition to browser validation.
