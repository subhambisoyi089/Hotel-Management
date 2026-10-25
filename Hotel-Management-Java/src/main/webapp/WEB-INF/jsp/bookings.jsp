<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/layout/taglibs.jspf" %>
<jsp:include page="/WEB-INF/jsp/layout/header.jsp">
  <jsp:param name="title" value="Bookings"/>
  <jsp:param name="active" value="bookings"/>
</jsp:include>

<header class="page-head">
  <div>
    <h1>Bookings</h1>
    <p class="muted">${bookings.size()} shown</p>
  </div>
  <a class="btn primary" href="${ctx}/bookings/new">New booking</a>
</header>

<nav class="filters" aria-label="Filter bookings">
  <a href="${ctx}/bookings" class="${empty selected ? 'on' : ''}">All</a>
  <c:forEach var="s" items="${statuses}">
    <a href="${ctx}/bookings?status=${s}" class="${selected == s ? 'on' : ''}"><c:out value="${s.label}"/></a>
  </c:forEach>
</nav>

<section class="panel">
  <c:choose>
    <c:when test="${not empty bookings}">
      <div class="table-wrap"><table>
        <thead><tr><th>Booking</th><th>Guest</th><th>Room</th><th>Stay</th><th>Status</th><th></th></tr></thead>
        <tbody>
          <c:forEach var="b" items="${bookings}">
            <tr>
              <td><c:out value="${b.code}"/></td>
              <td><strong><c:out value="${b.guest.name}"/></strong><br><span class="muted small"><c:out value="${b.guest.phone}"/></span></td>
              <td><c:out value="${b.room.roomNo}"/> <span class="muted small"><c:out value="${b.room.type.label}"/></span></td>
              <td><c:out value="${b.checkInText}"/> to <c:out value="${b.checkOutText}"/></td>
              <td><h:pill label="${b.status.label}" css="${b.status.css}"/></td>
              <td class="row-actions"><h:bookingActions b="${b}"/></td>
            </tr>
          </c:forEach>
        </tbody>
      </table></div>
    </c:when>
    <c:when test="${not empty selected}">
      <p class="empty">No <c:out value="${selected.label.toLowerCase()}"/> bookings right now.</p>
    </c:when>
    <c:otherwise>
      <p class="empty">No bookings yet. <a href="${ctx}/bookings/new">Create the first booking.</a></p>
    </c:otherwise>
  </c:choose>
</section>

<jsp:include page="/WEB-INF/jsp/layout/footer.jsp"/>
