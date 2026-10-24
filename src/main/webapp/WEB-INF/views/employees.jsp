<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Employees · PeopleDesk</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
    <script src="${pageContext.request.contextPath}/assets/app.js" defer></script>
</head>
<body>
<div class="app-shell">
    <header class="topbar">
        <a class="brand" href="${pageContext.request.contextPath}/employees"><span class="brand-mark">P</span>PeopleDesk</a>
        <div class="topbar-actions">
            <span class="user-label">Signed in as <c:out value="${sessionScope.userName}"/></span>
            <form class="inline-form" method="post" action="${pageContext.request.contextPath}/logout" data-prevent-double-submit>
                <input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>">
                <button class="button button-secondary button-small" type="submit">Sign out</button>
            </form>
        </div>
    </header>
    <main class="content">
        <c:if test="${not empty flash}"><div class="notice"><c:out value="${flash}"/></div></c:if>
        <div class="page-heading">
            <div><span class="eyebrow">YOUR WORKSPACE</span><h1>Employee directory</h1><p>Keep your team's details organized in one place.</p></div>
            <a class="button button-primary" href="${pageContext.request.contextPath}/employees/new">＋ Add employee</a>
        </div>
        <section class="table-card">
            <c:choose>
                <c:when test="${empty employees}">
                    <div class="empty-state">
                        <h2>Your directory starts here</h2>
                        <p>Add your first employee to get organized.</p>
                        <a class="button button-primary" href="${pageContext.request.contextPath}/employees/new">Add an employee</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead><tr><th>Name</th><th>Email</th><th>Department</th><th>Salary</th><th>Actions</th></tr></thead>
                        <tbody>
                        <c:forEach var="employee" items="${employees}">
                            <tr>
                                <td><strong><c:out value="${employee.name}"/></strong></td>
                                <td><c:out value="${employee.email}"/></td>
                                <td><c:out value="${employee.department}"/></td>
                                <td>$<c:out value="${employee.salary}"/></td>
                                <td><div class="row-actions">
                                    <a class="button button-secondary button-small" href="${pageContext.request.contextPath}/employees/edit?id=${employee.id}">Edit</a>
                                    <form class="inline-form" method="post" action="${pageContext.request.contextPath}/employees/delete" data-prevent-double-submit>
                                        <input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>">
                                        <input type="hidden" name="id" value="${employee.id}">
                                        <button class="button button-danger button-small" type="submit" onclick="return confirm('Delete this employee?')">Delete</button>
                                    </form>
                                </div></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </section>
    </main>
</div>
</body>
</html>
