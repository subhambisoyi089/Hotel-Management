<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="css" required="true" %>
<span class="pill s-<c:out value='${css}'/>"><c:out value="${label}"/></span>
