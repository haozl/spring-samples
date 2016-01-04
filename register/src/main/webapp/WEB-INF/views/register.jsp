<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Registeration</title>
    <link rel="stylesheet" href="<c:url value="/resources/css/w3.css" />">
    <script src="<c:url value="/resources/js/jquery.min.js" />"></script>
</head>
<body>
<c:url value="/login" var="loginUrl"/>
<c:url value="/register" var="registerUrl"/>
<c:url value="/captcha?" var="captchaUrl"/>
<div class="w3-content" style="max-width: 600px">


    <div class="w3-container">
        <div class="w3-container w3-blue">
            <h2>Registration</h2>
        </div>

        <form:form class="w3-container" modelAttribute="user" method="POST" enctype="utf8">
            <p>
                <form:label path="username" cssClass="w3-label"
                            cssErrorClass="">Username: </form:label>
                <form:input path="username" cssClass="w3-input"/>
                <form:errors path="username" cssClass="error"/>
            </p>
            <p>
                <form:label path="password" cssClass="w3-label"
                            cssErrorClass="">Password: </form:label>
                <form:password path="password" cssClass="w3-input"/>
                <form:errors path="password" cssClass="error"/>
            </p>
            <p>
                <form:label path="passwordConfirmation" cssClass="w3-label"
                            cssErrorClass="">Password Confirm: </form:label>
                <form:password path="passwordConfirmation" cssClass="w3-input"/>
                <form:errors path="passwordConfirmation" cssClass="error"/>
            </p>

            <p>
                <form:label path="email" cssClass="w3-label" cssErrorClass="">Email: </form:label>
                <form:input path="email" cssClass="w3-input"/>
                <form:errors path="email" cssClass="error"/>
            </p>

            <div class="w3-row">
                <p class="w3-half">
                    <form:label path="captcha" cssClass="w3-label" cssErrorClass="">Captcha: </form:label>
                    <form:input path="captcha" cssClass="w3-input"/>
                    <form:errors path="captcha" cssClass="error"/>
                </p>

                <p class="w3-half">
                    <img id="captcha_img" src="${captchaUrl}" alt="captcha" style="padding: 10px 30px">
                </p>
            </div>


            <a href="${loginUrl}" class="w3-right"><spring:message
                    code="form.login"></spring:message></a>
            <p>
                <button class="w3-btn w3-teal"><spring:message code="label.form.submit"></spring:message></button>
            </p>

        </form:form>
    </div>
</div>
<script>
    $(function () {
        $('#captcha_img').click(function () {
            var url = "${captchaUrl}" + Math.random();
            $(this).attr("src", url);
        });
    });
</script>
<style>
    .error {
        color: red;
    }
</style>
</body>
</html>
