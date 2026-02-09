<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

$isLoggedIn = isset($_SESSION['user_id']);
?>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="/">Barangay IMS</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <?php if ($isLoggedIn): ?>
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="/">Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/residents">Residents</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/households">Households</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/officials">Officials</a>
                    </li>
                </ul>
                <div class="d-flex">
                    <span class="navbar-text me-3">
                        <?= htmlspecialchars($_SESSION['username'] ?? 'User') ?>
                    </span>
                    <a href="/logout" class="btn btn-outline-light btn-sm">Logout</a>
                </div>
            <?php else: ?>
                <div class="ms-auto">
                    <a href="/login" class="btn btn-outline-light btn-sm">Login</a>
                </div>
            <?php endif; ?>
        </div>
    </div>
</nav>

