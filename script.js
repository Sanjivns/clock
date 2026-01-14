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

/* --- TABS --- */
const tabs = document.querySelectorAll(".tab-btn");
const tabContents = document.querySelectorAll(".tab-content");

tabs.forEach((tab) => {
  tab.addEventListener("click", () => {
    const targetId = tab.dataset.tab;

    // Update active tab button
    tabs.forEach((t) => t.classList.remove("active"));
    tab.classList.add("active");

    // Update active content
    tabContents.forEach((content) => {
      content.classList.remove("active");
      if (content.id === targetId) {
        content.classList.add("active");
      }
    });
  });
});

/* --- STOPWATCH --- */
let swStartTime = 0;
let swElapsedTime = 0;
let swInterval = null;

const swDisplay = document.getElementById("sw-display");
const swStartBtn = document.getElementById("sw-start");
const swStopBtn = document.getElementById("sw-stop");
const swResetBtn = document.getElementById("sw-reset");

function updateStopwatchDisplay() {
  const time = Date.now() - swStartTime + swElapsedTime;
  const ms = Math.floor((time % 1000) / 10);
  const totalSeconds = Math.floor(time / 1000);
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = totalSeconds % 60;
  const hours = Math.floor(minutes / 60);

  const format = (num) => String(num).padStart(2, "0");
  swDisplay.textContent = `${format(hours)}:${format(minutes % 60)}:${format(seconds)}.${format(ms)}`;
}

swStartBtn.addEventListener("click", () => {
  swStartTime = Date.now();
  swInterval = setInterval(updateStopwatchDisplay, 10);
  swStartBtn.disabled = true;
  swStopBtn.disabled = false;
});

swStopBtn.addEventListener("click", () => {
  clearInterval(swInterval);
  swElapsedTime += Date.now() - swStartTime;
  swStartBtn.disabled = false;
  swStopBtn.disabled = true;
});

swResetBtn.addEventListener("click", () => {
  clearInterval(swInterval);
  swElapsedTime = 0;
  swDisplay.textContent = "00:00:00.00";
  swStartBtn.disabled = false;
  swStopBtn.disabled = true;
});

/* --- TIMER --- */
let tmInterval = null;
let tmRemainingTime = 0;

const tmDisplay = document.getElementById("tm-display");
const tmInputs = document.getElementById("timer-inputs");
const tmInputHours = document.getElementById("tm-hours");
const tmInputMinutes = document.getElementById("tm-minutes");
const tmInputSeconds = document.getElementById("tm-seconds");
const tmStartBtn = document.getElementById("tm-start");
const tmPauseBtn = document.getElementById("tm-pause");
const tmResetBtn = document.getElementById("tm-reset");

function formatTimer(totalSeconds) {
  const h = Math.floor(totalSeconds / 3600);
  const m = Math.floor((totalSeconds % 3600) / 60);
  const s = totalSeconds % 60;
  return `${String(h).padStart(2, "0")}:${String(m).padStart(2, "0")}:${String(s).padStart(2, "0")}`;
}

function updateTimer() {
  if (tmRemainingTime <= 0) {
    clearInterval(tmInterval);
    tmDisplay.textContent = "Time's up!";
    // Play a sound or visual alert if needed
    tmStartBtn.style.display = "none";
    tmPauseBtn.style.display = "none";
    return;
  }
  tmRemainingTime--;
  tmDisplay.textContent = formatTimer(tmRemainingTime);
}

tmStartBtn.addEventListener("click", () => {
  if (!tmInterval) {
    // Starting fresh
    if (tmDisplay.style.display === "none") {
      const h = parseInt(tmInputHours.value) || 0;
      const m = parseInt(tmInputMinutes.value) || 0;
      const s = parseInt(tmInputSeconds.value) || 0;
      tmRemainingTime = h * 3600 + m * 60 + s;

      if (tmRemainingTime <= 0) return; // Don't start if 0

      // Switch to display mode
      tmInputs.style.display = "none";
      tmDisplay.style.display = "block";
      tmDisplay.textContent = formatTimer(tmRemainingTime);
    }
  }

  // Resume or Start
  tmInterval = setInterval(updateTimer, 1000);
  tmStartBtn.style.display = "none";
  tmPauseBtn.style.display = "inline-block";
});

tmPauseBtn.addEventListener("click", () => {
  clearInterval(tmInterval);
  tmInterval = null;
  tmStartBtn.style.display = "inline-block";
  tmStartBtn.textContent = "Resume";
  tmPauseBtn.style.display = "none";
});

tmResetBtn.addEventListener("click", () => {
  clearInterval(tmInterval);
  tmInterval = null;
  tmDisplay.style.display = "none";
  tmInputs.style.display = "flex";
  tmStartBtn.style.display = "inline-block";
  tmStartBtn.textContent = "Start";
  tmPauseBtn.style.display = "none";
  // Reset inputs ?? maybe keep them for creating same timer again
});

