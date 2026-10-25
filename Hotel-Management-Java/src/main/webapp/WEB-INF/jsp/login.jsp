<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/layout/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Sign in - <c:out value="${hotel.name}"/></title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Hanken+Grotesk:wght@400;500;600;700&family=Instrument+Serif&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<div class="login">
  <section class="login-brand">
    <h1><c:out value="${hotel.name}"/></h1>
    <p>Bookings, rooms, guests and bills for the front desk.</p>
  </section>
  <section class="login-panel">
    <form method="post" action="${ctx}/login" class="login-form">
      <input type="hidden" name="_csrf" value="<c:out value='${sessionScope.csrfToken}'/>">
      <h2>Sign in</h2>
      <c:if test="${not empty error}">
        <div class="flashes" role="status"><p class="flash error"><c:out value="${error}"/></p></div>
      </c:if>
      <label>Username
        <input type="text" name="username" autocomplete="username" required autofocus>
      </label>
      <label>Password
        <input type="password" name="password" autocomplete="current-password" required>
      </label>
      <button class="btn primary wide">Sign in</button>
    </form>
  </section>
</div>
</body>
</html>
