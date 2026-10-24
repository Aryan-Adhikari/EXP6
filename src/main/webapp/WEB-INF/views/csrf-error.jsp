<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Form expired · PeopleDesk</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
</head>
<body>
<main class="auth-layout">
    <section class="auth-card">
        <span class="eyebrow">PLEASE TRY AGAIN</span>
        <h1>This form is out of date.</h1>
        <div class="notice notice-error"><c:out value="${error}"/></div>
        <c:choose>
            <c:when test="${signedIn}">
                <a class="button button-primary" href="${pageContext.request.contextPath}/employees">Back to directory</a>
            </c:when>
            <c:otherwise>
                <a class="button button-primary" href="${pageContext.request.contextPath}/login">Reload sign in</a>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>
