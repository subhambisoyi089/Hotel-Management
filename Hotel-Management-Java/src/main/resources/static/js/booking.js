// New booking page: loads only the rooms that are free for the chosen dates.
(() => {
  const form = document.getElementById("booking-form");
  if (!form) return;

  const guestSelect = form.elements.guestId;
  const newGuest = document.getElementById("new-guest");
  const checkIn = form.elements.checkIn;
  const checkOut = form.elements.checkOut;
  const roomSelect = form.elements.roomId;
  const guestCount = form.elements.numGuests;
  const hint = document.getElementById("room-hint");
  const estimate = document.getElementById("estimate");
  let rooms = [];

  const inr = (n) => "\u20B9" + Number(n).toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });

  function toggleGuest() {
    const isNew = guestSelect.value === "new";
    newGuest.hidden = !isNew;
    form.elements.newName.required = isNew;
    form.elements.newPhone.required = isNew;
  }

  function nights() {
    const diff = (new Date(checkOut.value) - new Date(checkIn.value)) / 86400000;
    return Number.isFinite(diff) ? Math.max(Math.round(diff), 1) : 0;
  }

  function updateEstimate() {
    const room = rooms.find((r) => String(r.id) === roomSelect.value);
    if (!room || !nights()) { estimate.hidden = true; return; }
    guestCount.max = room.capacity;
    estimate.hidden = false;
    estimate.textContent = `${nights()} night${nights() > 1 ? "s" : ""} in room ${room.roomNo}: ${inr(nights() * room.price)} before GST.`;
  }

  async function loadRooms() {
    const wanted = roomSelect.value || roomSelect.dataset.selected;
    if (!checkIn.value || !checkOut.value || checkOut.value <= checkIn.value) {
      rooms = [];
      roomSelect.innerHTML = '<option value="">Pick dates first</option>';
      hint.textContent = checkIn.value && checkOut.value ? "Check-out must be after check-in." : "";
      updateEstimate();
      return;
    }
    hint.textContent = "Checking which rooms are free...";
    try {
      const res = await fetch(`${form.dataset.roomsUrl}?checkIn=${checkIn.value}&checkOut=${checkOut.value}`);
      if (!res.ok) throw new Error("bad response");
      rooms = await res.json();
    } catch (err) {
      hint.textContent = "Couldn't load rooms. Reload the page and try again.";
      return;
    }
    if (!rooms.length) {
      roomSelect.innerHTML = '<option value="">No room is free on these dates</option>';
      hint.textContent = "Try different dates.";
    } else {
      roomSelect.innerHTML = '<option value="" disabled>Choose a room</option>' + rooms.map((r) =>
        `<option value="${r.id}">${r.roomNo}, ${r.roomType}, sleeps ${r.capacity}, ${inr(r.price)} per night</option>`
      ).join("");
      roomSelect.value = rooms.some((r) => String(r.id) === wanted) ? wanted : "";
      hint.textContent = `${rooms.length} room${rooms.length > 1 ? "s" : ""} free.`;
    }
    updateEstimate();
  }

  checkIn.addEventListener("change", () => {
    if (checkIn.value) {
      const next = new Date(checkIn.value);
      next.setDate(next.getDate() + 1);
      checkOut.min = next.toISOString().slice(0, 10);
    }
    loadRooms();
  });
  checkOut.addEventListener("change", loadRooms);
  roomSelect.addEventListener("change", updateEstimate);
  guestSelect.addEventListener("change", toggleGuest);

  toggleGuest();
  loadRooms();
})();
