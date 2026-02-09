(() => {
  function normalize(s) {
    return (s ?? "").toString().trim().toLowerCase();
  }

  function setupTableFilter(key) {
    const input = document.querySelector(`[data-table-filter="${key}"]`);
    const clearBtn = document.querySelector(`[data-table-filter-clear="${key}"]`);
    const table = document.querySelector(`[data-table="${key}"]`);
    const tbody = table?.querySelector("tbody");

    if (!input || !table || !tbody) return;

    const dataRows = () => Array.from(tbody.querySelectorAll("tr[data-row]"));
    const noMatchRowId = `no-matches-${key}`;

    function ensureNoMatchRow() {
      let row = tbody.querySelector(`#${CSS.escape(noMatchRowId)}`);
      if (row) return row;

      row = document.createElement("tr");
      row.id = noMatchRowId;
      row.innerHTML = `
        <td colspan="99" class="text-center py-4 text-muted">
          No matches.
        </td>
      `;
      row.hidden = true;
      tbody.appendChild(row);
      return row;
    }

    const noMatchRow = ensureNoMatchRow();

    function applyFilter() {
      const q = normalize(input.value);
      let visibleCount = 0;

      for (const tr of dataRows()) {
        const match = q === "" || normalize(tr.textContent).includes(q);
        tr.hidden = !match;
        if (match) visibleCount += 1;
      }

      noMatchRow.hidden = q === "" || visibleCount > 0;
    }

    input.addEventListener("input", applyFilter);
    clearBtn?.addEventListener("click", () => {
      input.value = "";
      input.focus();
      applyFilter();
    });

    applyFilter();
  }

  document.addEventListener("DOMContentLoaded", () => {
    setupTableFilter("residents");

    // Simple navbar notification dropdown toggle (UI only for now).
    const toggle = document.querySelector("[data-notif-toggle]");
    const dropdown = document.getElementById("notifDropdown");

    if (toggle && dropdown) {
      toggle.addEventListener("click", (event) => {
        event.stopPropagation();
        dropdown.classList.toggle("show");
      });

      document.addEventListener("click", () => {
        dropdown.classList.remove("show");
      });
    }
  });
})();
