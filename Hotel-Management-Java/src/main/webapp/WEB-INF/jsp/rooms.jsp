<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/layout/taglibs.jspf" %>
<jsp:include page="/WEB-INF/jsp/layout/header.jsp">
  <jsp:param name="title" value="Rooms"/>
  <jsp:param name="active" value="rooms"/>
</jsp:include>

<header class="page-head">
  <div>
    <h1>Rooms</h1>
    <p class="muted">${rooms.size()} rooms in the hotel</p>
  </div>
  <button type="button" class="btn primary" data-open="room-dialog">Add room</button>
</header>

<section class="panel">
  <c:choose>
    <c:when test="${not empty rooms}">
      <div class="table-wrap"><table>
        <thead><tr><th>Room</th><th>Type</th><th>Price per night</th><th>Sleeps</th><th>Status</th><th></th></tr></thead>
        <tbody>
          <c:forEach var="r" items="${rooms}">
            <tr>
              <td><strong><c:out value="${r.roomNo}"/></strong></td>
              <td><c:out value="${r.type.label}"/></td>
              <td><h:money value="${r.pricePerNight}"/></td>
              <td>${r.capacity}</td>
              <td><h:pill label="${r.status.label}" css="${r.status.css}"/></td>
              <td class="row-actions">
                <button type="button" class="btn small ghost" data-open="room-dialog"
                        data-title="Edit room <c:out value='${r.roomNo}'/>"
                        data-id="${r.id}"
                        data-room-no="<c:out value='${r.roomNo}'/>"
                        data-type="${r.type}"
                        data-price="${r.pricePerNight}"
                        data-capacity="${r.capacity}"
                        data-status="${r.status}">Edit</button>
                <form method="post" action="${ctx}/rooms/${r.id}/delete" class="inline"
                      data-confirm="Delete room <c:out value='${r.roomNo}'/>?">
                  <input type="hidden" name="_csrf" value="<c:out value='${sessionScope.csrfToken}'/>">
                  <button class="btn small danger">Delete</button>
                </form>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table></div>
    </c:when>
    <c:otherwise><p class="empty">No rooms yet. Add a room to start taking bookings.</p></c:otherwise>
  </c:choose>
</section>

<dialog id="room-dialog">
  <form method="post" action="${ctx}/rooms/save" class="dialog-form">
    <input type="hidden" name="_csrf" value="<c:out value='${sessionScope.csrfToken}'/>">
    <input type="hidden" name="id" value="">
    <h2 data-text="title" data-fallback="Add room">Add room</h2>
    <div class="grid-2">
      <label>Room number
        <input type="text" name="roomNo" maxlength="10" required>
      </label>
      <label>Type
        <select name="type" required>
          <c:forEach var="t" items="${roomTypes}"><option value="${t}"><c:out value="${t.label}"/></option></c:forEach>
        </select>
      </label>
      <label>Price per night (₹)
        <input type="number" name="price" min="1" step="0.01" required>
      </label>
      <label>Sleeps
        <input type="number" name="capacity" min="1" max="12" value="2" required>
      </label>
    </div>
    <label>Status
      <select name="status">
        <option value="AVAILABLE">Available</option>
        <option value="MAINTENANCE">Maintenance</option>
        <option value="OCCUPIED" disabled>Occupied (set by check-in)</option>
      </select>
    </label>
    <div class="dialog-actions">
      <button type="button" class="btn ghost" data-close>Cancel</button>
      <button class="btn primary">Save room</button>
    </div>
  </form>
</dialog>

<jsp:include page="/WEB-INF/jsp/layout/footer.jsp"/>
