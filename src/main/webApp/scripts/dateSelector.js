function parseIsoLocal(iso) {
    var parts = iso.split("-");
    return new Date(parseInt(parts[0], 10), parseInt(parts[1], 10) - 1, parseInt(parts[2], 10));
}

function formatIso(date) {
    var year = date.getFullYear();
    var month = String(date.getMonth() + 1).padStart(2, "0");
    var day = String(date.getDate()).padStart(2, "0");
    return year + "-" + month + "-" + day;
}

function legacyToIso(value) {
    if (!value) {
        return "";
    }
    var v = String(value).replace(/^\s+|\s+$/g, "");
    if (/^\d{4}-\d{2}-\d{2}$/.test(v)) {
        return v;
    }
    if (/^\d{8}$/.test(v)) {
        return v.substring(0, 4) + "-" + v.substring(4, 6) + "-" + v.substring(6, 8);
    }
    var match = v.match(/^(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{4})$/);
    if (!match) {
        return "";
    }
    var day = ("0" + match[1]).slice(-2);
    var month = ("0" + match[2]).slice(-2);
    return match[3] + "-" + month + "-" + day;
}

function isoToLegacy(iso, separator) {
    if (!iso) {
        return "";
    }
    var parts = iso.split("-");
    if (parts.length !== 3) {
        return "";
    }
    return parts[2] + separator + parts[1] + separator + parts[0];
}

function refreshDateLabel(inputId, displayId) {
    var dateInput = document.getElementById(inputId);
    var label = document.getElementById(displayId);
    if (!dateInput || !label) {
        return;
    }
    if (!dateInput.value) {
        label.textContent = "";
        return;
    }
    var dateObj = parseIsoLocal(dateInput.value);
    label.textContent = dateObj.toLocaleDateString("fr-FR", {
        year: "numeric",
        month: "long",
        day: "numeric",
        weekday: "long"
    });
}

function shiftDate(inputId, days, displayId, afterChange) {
    var dateInput = document.getElementById(inputId);
    if (!dateInput || !dateInput.value) {
        return;
    }
    var currentDate = parseIsoLocal(dateInput.value);
    currentDate.setDate(currentDate.getDate() + days);
    dateInput.value = formatIso(currentDate);
    refreshDateLabel(inputId, displayId);
    if (typeof afterChange === "function") {
        afterChange();
    }
}

function initDateSelector(inputId, displayId, legacyValue) {
    var dateInput = document.getElementById(inputId);
    if (!dateInput) {
        return;
    }
    var iso = legacyToIso(legacyValue);
    if (iso) {
        dateInput.value = iso;
    }
    refreshDateLabel(inputId, displayId);
}
