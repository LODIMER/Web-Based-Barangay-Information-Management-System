<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

$isLoggedIn = isset($_SESSION['user_id']);
$role = $_SESSION['role'] ?? 'resident';
// $baseUrl is defined in the main layout and available here.
?>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="<?= $baseUrl ?: '/' ?>">Barangay IMS</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <?php if ($isLoggedIn): ?>
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="<?= $baseUrl ?: '/' ?>">Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<?= $baseUrl ?>/profile">My Profile</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<?= $baseUrl ?>/ayuda/request">
                            <?= ($role === 'official' || $role === 'admin') ? 'Add Ayuda' : 'Request Ayuda' ?>
                        </a>
                    </li>
                    <?php if ($role === 'official' || $role === 'admin'): ?>
                        <li class="nav-item">
                            <a class="nav-link" href="<?= $baseUrl ?>/schedule">Schedule</a>
                        </li>
                    <?php endif; ?>
                    <?php if ($role === 'official' || $role === 'admin'): ?>
                        <li class="nav-item">
                            <a class="nav-link" href="<?= $baseUrl ?>/residents">Residents</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<?= $baseUrl ?>/households">Households</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<?= $baseUrl ?>/officials">Officials</a>
                        </li>
                    <?php endif; ?>
                </ul>
                <div class="d-flex align-items-center gap-3">
                    <button
                        type="button"
                        class="btn btn-link nav-icon-btn position-relative p-0 border-0 text-white"
                        id="notifToggle"
                        data-notif-toggle
                    >
                        <span class="nav-bell-icon">🔔</span>
                        <span class="nav-badge-dot"></span>
                    </button>
                    <span class="navbar-text me-1 small">
                        <?= htmlspecialchars($_SESSION['username'] ?? 'User') ?>
                    </span>
                    <a href="<?= $baseUrl ?>/logout" class="btn btn-outline-light btn-sm">Logout</a>
                </div>
            <?php else: ?>
                <div class="ms-auto">
                    <a href="<?= $baseUrl ?>/login" class="btn btn-outline-light btn-sm me-2">Login</a>
                    <a href="<?= $baseUrl ?>/register" class="btn btn-light btn-sm text-primary">Register</a>
                </div>
            <?php endif; ?>

            <div class="notif-dropdown shadow-sm" id="notifDropdown">
                <div class="notif-header small fw-semibold text-muted px-3 py-2 border-bottom bg-light">
                    Notifications
                </div>
                <div class="notif-body small p-3">
                    <p class="mb-1"><strong>No new notifications.</strong></p>
                    <p class="mb-0 text-muted">Updates about your ayuda requests and schedules will appear here.</p>
                </div>
            </div>
        </div>
    </div>
</nav>

