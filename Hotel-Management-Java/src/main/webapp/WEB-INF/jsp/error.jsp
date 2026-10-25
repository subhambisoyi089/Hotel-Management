<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ include file="/WEB-INF/jsp/layout/taglibs.jspf" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Something went wrong</title>
  <link href="https://fonts.googleapis.com/css2?family=Hanken+Grotesk:wght@400;500;600;700&family=Instrument+Serif&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<main class="main" style="margin:0 auto;padding-top:15vh;max-width:560px">
  <h1>
    <c:choose>
      <c:when test="${status == 404}">That page doesn't exist</c:when>
      <c:when test="${status == 400}">That request couldn't be processed</c:when>
      <c:otherwise>Something went wrong</c:otherwise>
    </c:choose>
  </h1>
  <p class="muted" style="margin:12px 0 22px">
    <c:choose>
      <c:when test="${status == 400}">Your form may have expired. Go back, reload the page and try again.</c:when>
      <c:otherwise>Go back to the front desk and try again.</c:otherwise>
    </c:choose>
  </p>
  <a class="btn primary" href="${ctx}/">Back to the front desk</a>
</main>
</body>
</html>
