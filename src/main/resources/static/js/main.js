// Shared behaviour: dialogs (add / edit / check-out) and confirm prompts.
(() => {
  document.querySelectorAll("[data-open]").forEach((btn) => {
    btn.addEventListener("click", () => {
      const dialog = document.getElementById(btn.dataset.open);
      const form = dialog.querySelector("form");
      form.reset();

      // Copy data-* values into the form field with the same name (used for Edit buttons).
      Object.entries(btn.dataset).forEach(([key, value]) => {
        if (key === "open" || key === "action") return;
        const field = form.elements[key];
        if (field && "value" in field) field.value = value;
      });
      if (btn.dataset.action) form.action = btn.dataset.action;

      // Text slots such as the dialog title or the guest name on the check-out dialog.
      dialog.querySelectorAll("[data-text]").forEach((el) => {
        el.textContent = btn.dataset[el.dataset.text] || el.dataset.fallback || "";
      });

      dialog.showModal();
    });
  });

  document.querySelectorAll("dialog").forEach((dialog) => {
    dialog.querySelectorAll("[data-close]").forEach((b) => b.addEventListener("click", () => dialog.close()));
    // A click on the dimmed backdrop closes the dialog.
    dialog.addEventListener("click", (e) => { if (e.target === dialog) dialog.close(); });
  });

  document.querySelectorAll("form[data-confirm]").forEach((form) => {
    form.addEventListener("submit", (e) => {
      if (!window.confirm(form.dataset.confirm)) e.preventDefault();
    });
  });
})();
