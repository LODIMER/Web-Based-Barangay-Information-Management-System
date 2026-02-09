<div class="auth-card">
    <div class="auth-card-header">
        <div class="auth-card-icon">
            ❤
        </div>
        <div>
            <h1 class="auth-card-title mb-1">Create Resident Account</h1>
            <p class="auth-meta mb-0">Register to request ayuda and manage your records.</p>
        </div>
    </div>

    <?php if (!empty($errors)): ?>
        <div class="alert alert-danger py-2">
            <ul class="mb-0 small">
                <?php foreach ($errors as $message): ?>
                    <li><?= htmlspecialchars($message) ?></li>
                <?php endforeach; ?>
            </ul>
        </div>
    <?php endif; ?>

    <form method="post" action="<?= $baseUrl ?>/register" class="auth-form mt-3">
        <div class="mb-3">
            <label for="username" class="form-label auth-label">Username</label>
            <input
                type="text"
                name="username"
                id="username"
                class="form-control"
                required
                value="<?= htmlspecialchars($old['username'] ?? '') ?>"
            >
        </div>

        <div class="mb-3">
            <label for="full_name" class="form-label auth-label">Full Name</label>
            <input
                type="text"
                name="full_name"
                id="full_name"
                class="form-control"
                required
                value="<?= htmlspecialchars($old['full_name'] ?? '') ?>"
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
                minlength="6"
            >
        </div>

        <div class="mb-3">
            <label for="password_confirm" class="form-label auth-label">Confirm Password</label>
            <input
                type="password"
                name="password_confirm"
                id="password_confirm"
                class="form-control"
                required
                minlength="6"
            >
        </div>

        <button type="submit" class="btn btn-primary w-100 mb-2">
            Register
        </button>

        <p class="text-center small mb-0">
            Already have an account?
            <a href="<?= $baseUrl ?>/login">Back to login</a>
        </p>
    </form>
</div>

