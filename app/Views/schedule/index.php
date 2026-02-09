<div class="row g-4">
    <div class="col-lg-8">
        <div class="card shadow-sm border-0">
            <div class="card-body p-4">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <div>
                        <h1 class="h5 mb-1">My Schedule</h1>
                        <p class="text-muted small mb-0">
                            Approved ayuda requests will appear here with their appointment dates.
                        </p>
                    </div>
                    <div class="d-flex align-items-center gap-2">
                        <button class="btn btn-outline-secondary btn-sm" type="button">
                            ‹
                        </button>
                        <span class="small fw-semibold">February 2026</span>
                        <button class="btn btn-outline-secondary btn-sm" type="button">
                            ›
                        </button>
                    </div>
                </div>

                <div class="calendar-grid small">
                    <?php
                    // Simple static 7x6 grid; can be replaced with real calendar logic later.
                    $weekdays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
                    ?>
                    <div class="calendar-row calendar-header">
                        <?php foreach ($weekdays as $day): ?>
                            <div class="calendar-cell fw-semibold text-muted text-center"><?= $day ?></div>
                        <?php endforeach; ?>
                    </div>
                    <?php for ($row = 0; $row < 6; $row++): ?>
                        <div class="calendar-row">
                            <?php for ($col = 0; $col < 7; $col++): ?>
                                <div class="calendar-cell">
                                    <span class="calendar-date text-muted">–</span>
                                </div>
                            <?php endfor; ?>
                        </div>
                    <?php endfor; ?>
                </div>
            </div>
        </div>
    </div>

    <div class="col-lg-4">
        <div class="card shadow-sm border-0 mb-3">
            <div class="card-body p-3">
                <h2 class="h6 mb-2">Upcoming Schedules</h2>
                <?php if (!empty($upcoming)): ?>
                    <ul class="list-group list-group-flush small">
                        <?php foreach ($upcoming as $item): ?>
                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                <div>
                                    <div class="fw-semibold"><?= htmlspecialchars($item['title']) ?></div>
                                    <div class="text-muted"><?= htmlspecialchars($item['subtitle']) ?></div>
                                </div>
                                <span class="badge bg-success-subtle text-success"><?= htmlspecialchars($item['status']) ?></span>
                            </li>
                        <?php endforeach; ?>
                    </ul>
                <?php else: ?>
                    <p class="text-muted small mb-0">
                        No upcoming schedules. Approved requests will appear here.
                    </p>
                <?php endif; ?>
            </div>
        </div>
    </div>
</div>

