<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ attribute name="value" required="false" type="java.lang.Object" %>
<fmt:setLocale value="en_IN"/>
<fmt:formatNumber value="${empty value ? 0 : value}" type="currency" currencySymbol="₹"/>
