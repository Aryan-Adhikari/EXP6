<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Create account · PeopleDesk</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
    <script src="${pageContext.request.contextPath}/assets/app.js" defer></script>
</head>
<body>
<main class="auth-layout">
    <section class="auth-card">
        <a class="brand" href="${pageContext.request.contextPath}/"><span class="brand-mark">P</span>PeopleDesk</a>
        <h1>Create your workspace.</h1>
        <p>One account, one private employee directory.</p>
        <c:if test="${not empty error}"><div class="notice notice-error"><c:out value="${error}"/></div></c:if>
        <form class="form-stack" method="post" action="${pageContext.request.contextPath}/register" data-prevent-double-submit>
            <input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>">
            <label>Your name
                <input type="text" name="name" value="<c:out value='${param.name}'/>" maxlength="100" autocomplete="name" required>
            </label>
            <label>Email address
                <input type="email" name="email" value="<c:out value='${param.email}'/>" maxlength="190" autocomplete="email" required>
            </label>
            <label>Password
                <input type="password" name="password" minlength="8" autocomplete="new-password" required>
            </label>
            <p class="help-text">Use at least 8 characters (72 UTF-8 bytes maximum).</p>
            <button class="button button-primary" type="submit">Create account</button>
        </form>
        <p class="auth-footer">Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in</a></p>
    </section>
</main>
</body>
</html>
