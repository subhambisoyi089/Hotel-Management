<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/layout/taglibs.jspf" %>
<jsp:include page="/WEB-INF/jsp/layout/header.jsp">
  <jsp:param name="title" value="Guests"/>
  <jsp:param name="active" value="guests"/>
</jsp:include>

<header class="page-head">
  <div>
    <h1>Guests</h1>
    <p class="muted">${guests.size()} <c:out value="${empty q ? 'on record' : 'match'}"/></p>
  </div>
  <button type="button" class="btn primary" data-open="guest-dialog">Add guest</button>
</header>

<form method="get" action="${ctx}/guests" class="search">
  <input type="search" name="q" value="<c:out value='${q}'/>" placeholder="Search by name or phone" aria-label="Search guests">
  <button class="btn ghost">Search</button>
  <c:if test="${not empty q}"><a href="${ctx}/guests" class="link-btn">Clear</a></c:if>
</form>

<section class="panel">
  <c:choose>
    <c:when test="${not empty guests}">
      <div class="table-wrap"><table>
        <thead><tr><th>Name</th><th>Phone</th><th>Email</th><th>ID proof</th><th>Stays</th><th></th></tr></thead>
        <tbody>
          <c:forEach var="g" items="${guests}">
            <tr>
              <td><strong><c:out value="${g.name}"/></strong></td>
              <td><c:out value="${g.phone}"/></td>
              <td><c:out value="${empty g.email ? '-' : g.email}"/></td>
              <td><c:out value="${empty g.idProof ? '-' : g.idProof}"/></td>
              <td>${stays[g.id]}</td>
              <td class="row-actions">
                <button type="button" class="btn small ghost" data-open="guest-dialog"
                        data-title="Edit <c:out value='${g.name}'/>"
                        data-id="${g.id}"
                        data-name="<c:out value='${g.name}'/>"
                        data-phone="<c:out value='${g.phone}'/>"
                        data-email="<c:out value='${g.email}'/>"
                        data-id-proof="<c:out value='${g.idProof}'/>"
                        data-address="<c:out value='${g.address}'/>">Edit</button>
                <form method="post" action="${ctx}/guests/${g.id}/delete" class="inline"
                      data-confirm="Delete <c:out value='${g.name}'/>?">
                  <input type="hidden" name="_csrf" value="<c:out value='${sessionScope.csrfToken}'/>">
                  <button class="btn small danger">Delete</button>
                </form>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table></div>
    </c:when>
    <c:when test="${not empty q}">
      <p class="empty">No guest matches "<c:out value="${q}"/>". Try part of the name or the phone number.</p>
    </c:when>
    <c:otherwise>
      <p class="empty">No guests yet. They are added when you make a booking, or you can add one here.</p>
    </c:otherwise>
  </c:choose>
</section>

<dialog id="guest-dialog">
  <form method="post" action="${ctx}/guests/save" class="dialog-form">
    <input type="hidden" name="_csrf" value="<c:out value='${sessionScope.csrfToken}'/>">
    <input type="hidden" name="id" value="">
    <h2 data-text="title" data-fallback="Add guest">Add guest</h2>
    <div class="grid-2">
      <label>Full name
        <input type="text" name="name" maxlength="80" required>
      </label>
      <label>Phone
        <input type="tel" name="phone" maxlength="20" required>
      </label>
      <label>Email
        <input type="email" name="email" maxlength="80">
      </label>
      <label>ID proof (Aadhaar, passport, licence)
        <input type="text" name="idProof" maxlength="60">
      </label>
    </div>
    <label>Address
      <textarea name="address" rows="2" maxlength="200"></textarea>
    </label>
    <div class="dialog-actions">
      <button type="button" class="btn ghost" data-close>Cancel</button>
      <button class="btn primary">Save guest</button>
    </div>
  </form>
</dialog>

<jsp:include page="/WEB-INF/jsp/layout/footer.jsp"/>
