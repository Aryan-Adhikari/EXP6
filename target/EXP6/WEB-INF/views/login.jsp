<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sign in · PeopleDesk</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
    <script src="${pageContext.request.contextPath}/assets/app.js" defer></script>
</head>
<body>
<main class="auth-layout">
    <section class="auth-card">
        <a class="brand" href="${pageContext.request.contextPath}/"><span class="brand-mark">P</span>PeopleDesk</a>
        <h1>Welcome back.</h1>
        <p>Sign in to your employee workspace.</p>
        <c:if test="${param.registered == '1'}"><div class="notice">Account created. You can sign in now.</div></c:if>
        <c:if test="${param.expired == '1'}"><div class="notice">That sign-in form expired or was already submitted. Please try again.</div></c:if>
        <c:if test="${not empty error}"><div class="notice notice-error"><c:out value="${error}"/></div></c:if>
        <form class="form-stack" method="post" action="${pageContext.request.contextPath}/login" data-prevent-double-submit>
            <input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>">
            <label>Email address
                <input type="email" name="email" value="<c:out value='${param.email}'/>" maxlength="190" autocomplete="email" required>
            </label>
            <label>Password
                <input type="password" name="password" autocomplete="current-password" required>
            </label>
            <button class="button button-primary" type="submit">Sign in</button>
        </form>
        <p class="auth-footer">New to PeopleDesk? <a href="${pageContext.request.contextPath}/register">Create an account</a></p>
    </section>
</main>
</body>
</html>
