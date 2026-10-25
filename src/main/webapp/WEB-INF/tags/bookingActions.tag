<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="b" required="true" type="com.hotel.entity.Booking" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:choose>
  <c:when test="${b.booked}">
    <form method="post" action="${ctx}/bookings/${b.id}/checkin" class="inline">
      <input type="hidden" name="_csrf" value="<c:out value='${sessionScope.csrfToken}'/>">
      <button class="btn small primary">Check in</button>
    </form>
    <form method="post" action="${ctx}/bookings/${b.id}/cancel" class="inline" data-confirm="Cancel this booking?">
      <input type="hidden" name="_csrf" value="<c:out value='${sessionScope.csrfToken}'/>">
      <button class="btn small ghost">Cancel</button>
    </form>
  </c:when>
  <c:when test="${b.checkedIn}">
    <button type="button" class="btn small primary" data-open="checkout-dialog"
            data-action="${ctx}/bookings/${b.id}/checkout"
            data-summary="<c:out value='${b.guest.name}'/>, room <c:out value='${b.room.roomNo}'/>">Check out</button>
  </c:when>
  <c:when test="${b.checkedOut}">
    <a class="btn small ghost" href="${ctx}/bookings/${b.id}/invoice">Invoice</a>
  </c:when>
</c:choose>
