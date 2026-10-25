<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/layout/taglibs.jspf" %>
<jsp:include page="/WEB-INF/jsp/layout/header.jsp">
  <jsp:param name="title" value="Invoice"/>
  <jsp:param name="active" value="bookings"/>
</jsp:include>

<header class="page-head no-print">
  <div>
    <h1>Invoice</h1>
    <p class="muted">Print it or save it as a PDF from the print dialog.</p>
  </div>
  <div class="head-actions">
    <a class="btn ghost" href="${ctx}/bookings">Back to bookings</a>
    <button type="button" class="btn primary" onclick="window.print()">Print invoice</button>
  </div>
</header>

<article class="invoice">
  <header class="invoice-top">
    <div>
      <h2><c:out value="${hotel.name}"/></h2>
      <p><c:out value="${hotel.address}"/><br><c:out value="${hotel.phone}"/><br><c:out value="${hotel.gstin}"/></p>
    </div>
    <dl class="invoice-meta">
      <div><dt>Invoice</dt><dd><c:out value="${b.invoiceNo}"/></dd></div>
      <div><dt>Booking</dt><dd><c:out value="${b.code}"/></dd></div>
      <div><dt>Billed on</dt><dd><c:out value="${b.checkedOutOnText}"/></dd></div>
    </dl>
  </header>

  <section class="invoice-guest">
    <h3>Billed to</h3>
    <p><strong><c:out value="${b.guest.name}"/></strong><br>
       <c:out value="${b.guest.phone}"/>
       <c:if test="${not empty b.guest.email}"><br><c:out value="${b.guest.email}"/></c:if>
       <c:if test="${not empty b.guest.address}"><br><c:out value="${b.guest.address}"/></c:if></p>
    <p class="muted">Room <c:out value="${b.room.roomNo}"/> (<c:out value="${b.room.type.label}"/>),
       <c:out value="${b.checkInText}"/> to <c:out value="${b.checkOutText}"/></p>
  </section>

  <table class="invoice-lines">
    <thead><tr><th>Description</th><th class="num">Nights</th><th class="num">Rate</th><th class="num">Amount</th></tr></thead>
    <tbody>
      <tr>
        <td>Room <c:out value="${b.room.roomNo}"/>, <c:out value="${b.room.type.label}"/></td>
        <td class="num">${bill.nights}</td>
        <td class="num"><h:money value="${b.rate}"/></td>
        <td class="num"><h:money value="${bill.roomCharge}"/></td>
      </tr>
      <c:if test="${bill.hasExtra}">
        <tr>
          <td>Extra charges<c:if test="${not empty b.extraNote}">: <c:out value="${b.extraNote}"/></c:if></td>
          <td class="num"></td><td class="num"></td>
          <td class="num"><h:money value="${bill.extra}"/></td>
        </tr>
      </c:if>
    </tbody>
    <tfoot>
      <tr><td colspan="3">Subtotal</td><td class="num"><h:money value="${bill.subtotal}"/></td></tr>
      <tr><td colspan="3">GST at <c:out value="${hotel.gstPercent}"/>%</td><td class="num"><h:money value="${bill.gst}"/></td></tr>
      <tr class="grand"><td colspan="3">Total to pay</td><td class="num"><h:money value="${bill.total}"/></td></tr>
    </tfoot>
  </table>

  <p class="invoice-foot">Thank you for staying with us.</p>
</article>

<jsp:include page="/WEB-INF/jsp/layout/footer.jsp"/>
