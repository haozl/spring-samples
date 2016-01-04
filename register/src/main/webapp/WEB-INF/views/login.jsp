<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<c:url value="/resources/css/w3.css" />">
</head>
<body>

<c:url value="/login" var="loginUrl"/>
<c:url value="/register" var="registerUrl"/>
<div class="w3-content" style="max-width: 600px">

    <div class="w3-container">
        <div class="w3-container w3-blue">
            <h2>Login</h2>
        </div>

        <form:form class="w3-container" method="POST" enctype="utf8">
            <c:if test="${param.logout!= null}">
                <p class="w3-green">You have been logged out.</p>
            </c:if>
            <c:if test="${param.error!= null}">
                <p class="w3-red">Invalid username and password.</p>
            </c:if>
            <p>
                <label for="username" class="w3-label">Username</label>
                <input type="text" name="username" class="w3-input" id="username" placeholder="Username">
            </p>
            <p>
                <label for="password" class="w3-label">Username</label>
                <input type="password" name="password" class="w3-input" id="password" placeholder="Password">
            </p>

            <input class="w3-check" name="remember-me" type="checkbox">
            <label class="w3-validate">Remember me</label>
            </p>
            <a href="${registerUrl}" class="w3-right"><spring:message
                    code="label.form.signup"></spring:message></a>
            <p>
            <p>
                <button class="w3-btn w3-teal"><spring:message code="form.login"></spring:message></button>
            </p>
        </form:form>
    </div>

</div>

</body>
</html>
