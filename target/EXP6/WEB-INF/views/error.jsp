<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Request error · PeopleDesk</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
</head>
<body>
<main class="auth-layout"><section class="auth-card">
    <span class="eyebrow">SOMETHING WENT WRONG</span>
    <h1>We couldn't complete that.</h1>
    <div class="notice notice-error"><c:out value="${error}"/></div>
    <a class="button button-primary" href="${pageContext.request.contextPath}/employees">Back to directory</a>
</section></main>
</body>
</html>
