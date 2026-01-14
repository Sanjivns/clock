let is24Hour = true;

function formatTime(date, use24Hour) {
  let hours = date.getHours(); // local time by default
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");

  if (use24Hour) {
    const hh = String(hours).padStart(2, "0");
    return `${hh}:${minutes}:${seconds}`;
  }

  const suffix = hours >= 12 ? "PM" : "AM";
  hours = hours % 12 || 12; // convert 0 → 12
  const hh12 = String(hours).padStart(2, "0");
  return `${hh12}:${minutes}:${seconds} ${suffix}`;
}

function formatDate(date) {
  const options = {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  };
  return date.toLocaleDateString(undefined, options);
}

function getLocalTimeZoneLabel(date) {
  try {
    const formatter = new Intl.DateTimeFormat(undefined, {
      timeZoneName: "short",
    });
    const parts = formatter.formatToParts(date);
    const tzPart = parts.find((p) => p.type === "timeZoneName");
    return tzPart ? tzPart.value : "";
  } catch (e) {
    return "";
  }
}

function updateToggleLabel(button) {
  if (!button) return;
  button.textContent = is24Hour ? "Switch to 12-hour" : "Switch to 24-hour";
}

function updateClock() {
  const now = new Date(); // uses local timezone by default
  const clockEl = document.getElementById("clock");
  const dateEl = document.getElementById("date");
  const tzEl = document.getElementById("timezone");

  if (!clockEl || !dateEl) return;

  clockEl.textContent = formatTime(now, is24Hour);
  dateEl.textContent = formatDate(now);

  if (tzEl) {
    const tz = getLocalTimeZoneLabel(now);
    tzEl.textContent = tz ? `Local time zone: ${tz}` : "";
  }
}

// Set up toggle button
const toggleBtn = document.getElementById("formatToggle");
if (toggleBtn) {
  toggleBtn.addEventListener("click", () => {
    is24Hour = !is24Hour;
    updateToggleLabel(toggleBtn);
    updateClock();
  });
  updateToggleLabel(toggleBtn);
}

// Initial render
updateClock();
// Update every second
setInterval(updateClock, 1000);

