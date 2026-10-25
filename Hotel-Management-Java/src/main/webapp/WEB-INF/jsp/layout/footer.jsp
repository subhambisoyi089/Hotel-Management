<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/layout/taglibs.jspf" %>
  </main>
</div>

<dialog id="checkout-dialog">
  <form method="post" class="dialog-form">
    <input type="hidden" name="_csrf" value="<c:out value='${sessionScope.csrfToken}'/>">
    <h2>Check out</h2>
    <p class="muted" data-text="summary"></p>
    <label>Extra charges (food, laundry, other)
      <input type="number" name="extraCharges" min="0" step="0.01" value="0">
    </label>
    <label>What were they for? (optional)
      <input type="text" name="extraNote" maxlength="120" placeholder="Room service, laundry">
    </label>
    <p class="muted small">The room charge and <c:out value="${hotel.gstPercent}"/>% GST are added automatically.</p>
    <div class="dialog-actions">
      <button type="button" class="btn ghost" data-close>Cancel</button>
      <button class="btn primary">Check out and create bill</button>
    </div>
  </form>
</dialog>

<script src="${ctx}/js/main.js"></script>
</body>
</html>
