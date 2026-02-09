<?php $role = $role ?? 'resident'; ?>

<?php if ($role === 'resident'): ?>
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
                    <h2 class="h6 mb-2">Notifications</h2>
                    <p class="text-muted small mb-1">
                        No new notifications.
                    </p>
                    <p class="text-muted small mb-0">
                        Updates about your ayuda requests and schedules will appear here.
                    </p>
                </div>
            </div>

            <div class="card shadow-sm border-0">
                <div class="card-body p-3">
                    <h2 class="h6 mb-2">Quick Links</h2>
                    <ul class="list-unstyled small mb-0">
                        <li class="mb-1">
                            <a href="<?= $baseUrl ?>/ayuda/request">Request Ayuda</a>
                        </li>
                        <li class="mb-1">
                            <a href="<?= $baseUrl ?>/schedule">View Full Schedule</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
<?php else: ?>
    <div class="row g-4">
        <div class="col-md-4">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h6 class="card-subtitle mb-2 text-muted">Total Residents</h6>
                    <h2 class="card-title"><?= (int) $residentCount ?></h2>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h6 class="card-subtitle mb-2 text-muted">Total Households</h6>
                    <h2 class="card-title"><?= (int) $householdCount ?></h2>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h6 class="card-subtitle mb-2 text-muted">Barangay Officials</h6>
                    <h2 class="card-title"><?= (int) $officialCount ?></h2>
                </div>
            </div>
        </div>
    </div>
<?php endif; ?>

