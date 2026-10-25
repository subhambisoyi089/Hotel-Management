<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/layout/taglibs.jspf" %>
<jsp:include page="/WEB-INF/jsp/layout/header.jsp">
  <jsp:param name="title" value="New booking"/>
  <jsp:param name="active" value="bookings"/>
</jsp:include>

<header class="page-head">
  <div>
    <h1>New booking</h1>
    <p class="muted">Pick the dates first, then choose from the rooms that are free.</p>
  </div>
</header>

<section class="panel narrow">
  <form method="post" action="${ctx}/bookings/new" id="booking-form" class="stack"
        data-rooms-url="${ctx}/api/available-rooms">
    <input type="hidden" name="_csrf" value="<c:out value='${sessionScope.csrfToken}'/>">

    <fieldset>
      <legend>Guest</legend>
      <label>Who is staying?
        <select name="guestId" required>
          <option value="" disabled ${empty form.guestId ? 'selected' : ''}>Choose a guest</option>
          <c:forEach var="g" items="${guests}">
            <option value="${g.id}" ${form.guestId == fn:trim(g.id) ? 'selected' : ''}><c:out value="${g.name}"/>, <c:out value="${g.phone}"/></option>
          </c:forEach>
          <option value="new" ${form.guestId == 'new' ? 'selected' : ''}>Add a new guest</option>
        </select>
      </label>
      <div id="new-guest" class="grid-2" hidden>
        <label>Full name
          <input type="text" name="newName" maxlength="80" value="<c:out value='${form.newName}'/>">
        </label>
        <label>Phone
          <input type="tel" name="newPhone" maxlength="20" value="<c:out value='${form.newPhone}'/>">
        </label>
        <label>Email
          <input type="email" name="newEmail" maxlength="80" value="<c:out value='${form.newEmail}'/>">
        </label>
        <label>ID proof
          <input type="text" name="newIdProof" maxlength="60" value="<c:out value='${form.newIdProof}'/>">
        </label>
      </div>
    </fieldset>

    <fieldset>
      <legend>Stay</legend>
      <div class="grid-2">
        <label>Check-in
          <input type="date" name="checkIn" min="<c:out value='${today}'/>" value="<c:out value='${form.checkIn}'/>" required>
        </label>
        <label>Check-out
          <input type="date" name="checkOut" min="<c:out value='${today}'/>" value="<c:out value='${form.checkOut}'/>" required>
        </label>
      </div>
      <div class="grid-2">
        <label>Room
          <select name="roomId" data-selected="<c:out value='${form.roomId}'/>" required>
            <option value="">Pick dates first</option>
          </select>
        </label>
        <label>Number of guests
          <input type="number" name="numGuests" min="1" max="12" value="<c:out value='${empty form.numGuests ? 1 : form.numGuests}'/>" required>
        </label>
      </div>
      <p class="muted small" id="room-hint"></p>
      <p class="estimate" id="estimate" hidden></p>
    </fieldset>

    <div class="form-actions">
      <a class="btn ghost" href="${ctx}/bookings">Cancel</a>
      <button class="btn primary">Save booking</button>
    </div>
  </form>
</section>

<script src="${ctx}/js/booking.js"></script>
<jsp:include page="/WEB-INF/jsp/layout/footer.jsp"/>
