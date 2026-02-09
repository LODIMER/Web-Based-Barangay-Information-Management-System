<div class="auth-card">
    <div class="auth-card-header">
        <div class="auth-card-icon">
            ☺
        </div>
        <div>
            <h1 class="auth-card-title mb-1">Resident Login</h1>
            <p class="auth-meta mb-0">Access your barangay information dashboard.</p>
        </div>
    </div>

    <?php if (!empty($success)): ?>
        <div class="alert alert-success py-2"><?= htmlspecialchars($success) ?></div>
    <?php endif; ?>

    <?php if (!empty($error)): ?>
        <div class="alert alert-danger py-2"><?= htmlspecialchars($error) ?></div>
    <?php endif; ?>

    <form method="post" action="<?= $baseUrl ?>/login" class="auth-form mt-3">
        <div class="mb-3">
            <label for="username" class="form-label auth-label">Username</label>
            <input
                type="text"
                name="username"
                id="username"
                class="form-control"
                required
                autofocus
            >
        </div>
        <div class="mb-3">
            <label for="password" class="form-label auth-label">Password</label>
            <input
                type="password"
                name="password"
                id="password"
                class="form-control"
                required
            >
        </div>
        <button type="submit" class="btn btn-primary w-100 mt-2">
            Login
        </button>
        <p class="text-center small mt-3 mb-0">
            No account yet?
            <a href="<?= $baseUrl ?>/register">Create one</a>
        </p>
    </form>
</div>

