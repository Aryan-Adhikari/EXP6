<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:choose><c:when test="${editing}">Edit employee</c:when><c:otherwise>Add employee</c:otherwise></c:choose> · PeopleDesk</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
</head>
<body>
<div class="app-shell">
    <header class="topbar">
        <a class="brand" href="${pageContext.request.contextPath}/employees"><span class="brand-mark">P</span>PeopleDesk</a>
        <a class="button button-secondary button-small" href="${pageContext.request.contextPath}/employees">Back to directory</a>
    </header>
    <main class="content">
        <div class="page-heading"><div><span class="eyebrow">EMPLOYEE RECORD</span><h1><c:choose><c:when test="${editing}">Edit employee</c:when><c:otherwise>Add an employee</c:otherwise></c:choose></h1><p>Enter the details below. All fields are required.</p></div></div>
        <section class="form-card">
            <c:if test="${not empty error}"><div class="notice notice-error"><c:out value="${error}"/></div></c:if>
            <form method="post" action="${pageContext.request.contextPath}${formAction}" data-prevent-double-submit>
                <input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>">
                <div class="form-grid">
                    <label>Full name
                        <input type="text" name="name" value="<c:out value='${employee.name}'/>" maxlength="100" required>
                    </label>
                    <label>Email address
                        <input type="email" name="email" value="<c:out value='${employee.email}'/>" maxlength="190" required>
                    </label>
                    <label>Department
                        <input type="text" name="department" value="<c:out value='${employee.department}'/>" maxlength="100" required>
                    </label>
                    <label>Annual salary
                        <input type="number" name="salary" value="<c:out value='${employee.salary}'/>" min="0" max="9999999999.99" step="0.01" required>
                    </label>
                </div>
                <div class="form-actions">
                    <button class="button button-primary" type="submit"><c:choose><c:when test="${editing}">Save changes</c:when><c:otherwise>Add employee</c:otherwise></c:choose></button>
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/employees">Cancel</a>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>
