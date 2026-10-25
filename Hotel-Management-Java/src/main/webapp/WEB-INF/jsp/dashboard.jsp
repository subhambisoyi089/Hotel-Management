<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/layout/taglibs.jspf" %>
<jsp:include page="/WEB-INF/jsp/layout/header.jsp">
  <jsp:param name="title" value="Front desk"/>
  <jsp:param name="active" value="dashboard"/>
</jsp:include>

<header class="page-head">
  <div>
    <h1>Front desk</h1>
    <p class="muted"><c:out value="${todayText}"/></p>
  </div>
  <a class="btn primary" href="${ctx}/bookings/new">New booking</a>
</header>

<dl class="strip">
  <div><dt>Occupancy</dt><dd>${d.occupancy}%</dd><small>${d.occupied} of ${d.total} rooms</small></div>
  <div><dt>Free tonight</dt><dd>${d.available}</dd><small>${d.maintenance} in maintenance</small></div>
  <div><dt>Waiting to check in</dt><dd>${d.arrivals.size()}</dd><small>due today or earlier</small></div>
  <div><dt>Due to check out</dt><dd>${d.departures.size()}</dd><small>due today or earlier</small></div>
  <div><dt>Billed this month</dt><dd><h:money value="${d.revenue}"/></dd><small>including GST</small></div>
</dl>

<section class="panel">
  <div class="panel-head">
    <h2>Room board</h2>
    <ul class="legend">
      <li class="s-available">Available</li>
      <li class="s-occupied">Occupied</li>
      <li class="s-maintenance">Maintenance</li>
    </ul>
  </div>
  <c:choose>
    <c:when test="${not empty d.rooms}">
      <div class="board">
        <c:forEach var="r" items="${d.rooms}">
          <c:choose>
            <c:when test="${r.available}">
              <a class="tag s-available" href="${ctx}/bookings/new?room=${r.id}" title="Book room <c:out value='${r.roomNo}'/>">
                <span class="no"><c:out value="${r.roomNo}"/></span>
                <span class="kind"><c:out value="${r.type.label}"/></span>
                <span class="who">Available</span>
              </a>
            </c:when>
            <c:otherwise>
              <div class="tag s-${r.status.css}">
                <span class="no"><c:out value="${r.roomNo}"/></span>
                <span class="kind"><c:out value="${r.type.label}"/></span>
                <span class="who">
                  <c:choose>
                    <c:when test="${not empty d.guestByRoom[r.id]}"><c:out value="${d.guestByRoom[r.id]}"/></c:when>
                    <c:otherwise><c:out value="${r.status.label}"/></c:otherwise>
                  </c:choose>
                </span>
              </div>
            </c:otherwise>
          </c:choose>
        </c:forEach>
      </div>
    </c:when>
    <c:otherwise>
      <p class="empty">No rooms yet. <a href="${ctx}/rooms">Add your first room.</a></p>
    </c:otherwise>
  </c:choose>
</section>

<div class="two-col">
  <section class="panel">
    <h2>Waiting to check in</h2>
    <c:choose>
      <c:when test="${not empty d.arrivals}">
        <div class="table-wrap"><table>
          <thead><tr><th>Guest</th><th>Room</th><th>Arrives</th><th></th></tr></thead>
          <tbody>
            <c:forEach var="b" items="${d.arrivals}">
              <tr>
                <td><c:out value="${b.guest.name}"/></td>
                <td><c:out value="${b.room.roomNo}"/></td>
                <td><c:out value="${b.checkInText}"/></td>
                <td class="row-actions"><h:bookingActions b="${b}"/></td>
              </tr>
            </c:forEach>
          </tbody>
        </table></div>
      </c:when>
      <c:otherwise><p class="empty">No guests waiting. Bookings appear here on their arrival date.</p></c:otherwise>
    </c:choose>
  </section>

  <section class="panel">
    <h2>Due to check out</h2>
    <c:choose>
      <c:when test="${not empty d.departures}">
        <div class="table-wrap"><table>
          <thead><tr><th>Guest</th><th>Room</th><th>Leaves</th><th></th></tr></thead>
          <tbody>
            <c:forEach var="b" items="${d.departures}">
              <tr>
                <td><c:out value="${b.guest.name}"/></td>
                <td><c:out value="${b.room.roomNo}"/></td>
                <td><c:out value="${b.checkOutText}"/></td>
                <td class="row-actions"><h:bookingActions b="${b}"/></td>
              </tr>
            </c:forEach>
          </tbody>
        </table></div>
      </c:when>
      <c:otherwise><p class="empty">Nobody is due to leave. Guests show up here on their check-out date.</p></c:otherwise>
    </c:choose>
  </section>
</div>

<jsp:include page="/WEB-INF/jsp/layout/footer.jsp"/>
