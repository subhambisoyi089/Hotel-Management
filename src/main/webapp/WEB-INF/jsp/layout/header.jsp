<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/layout/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><c:out value="${param.title}"/> - <c:out value="${hotel.name}"/></title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Hanken+Grotesk:wght@400;500;600;700&family=Instrument+Serif&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<div class="shell">
  <aside class="side">
    <a class="brand" href="${ctx}/"><c:out value="${hotel.name}"/></a>
    <nav aria-label="Main">
      <a href="${ctx}/" class="${param.active == 'dashboard' ? 'on' : ''}">Front desk</a>
      <a href="${ctx}/bookings" class="${param.active == 'bookings' ? 'on' : ''}">Bookings</a>
      <a href="${ctx}/rooms" class="${param.active == 'rooms' ? 'on' : ''}">Rooms</a>
      <a href="${ctx}/guests" class="${param.active == 'guests' ? 'on' : ''}">Guests</a>
    </nav>
    <form method="post" action="${ctx}/logout" class="signout">
      <input type="hidden" name="_csrf" value="<c:out value='${sessionScope.csrfToken}'/>">
      <span><c:out value="${sessionScope.username}"/></span>
      <button class="link-btn">Sign out</button>
    </form>
  </aside>

  <main class="main">
    <c:if test="${not empty success or not empty error}">
      <div class="flashes" role="status">
        <c:if test="${not empty success}"><p class="flash success"><c:out value="${success}"/></p></c:if>
        <c:if test="${not empty error}"><p class="flash error"><c:out value="${error}"/></p></c:if>
      </div>
    </c:if>
