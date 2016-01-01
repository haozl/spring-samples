<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<sec:authentication var="user" property="principal"/>
<h2>Restricted area</h2>

<h3>${user}</h3>

<c:url value="/admin" var="adminUrl"/>
<h3><a href="${adminUrl}">Admin Area</a></h3>
<c:url value="/logout" var="logoutUrl"/>
<sf:form action="${logoutUrl}" method="post">
    <input type="submit" value="Logout">
</sf:form>
</body>
</html>