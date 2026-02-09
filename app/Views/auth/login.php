<div class="row justify-content-center">
    <div class="col-md-4">
        <div class="card shadow-sm border-0 mt-5">
            <div class="card-body">
                <h1 class="h4 mb-3 text-center">Barangay IMS Login</h1>

                <?php if (!empty($error)): ?>
                    <div class="alert alert-danger py-2"><?= htmlspecialchars($error) ?></div>
                <?php endif; ?>

                <form method="post" action="/login">
                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input type="text" name="username" id="username" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" name="password" id="password" class="form-control" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Login</button>
                </form>
            </div>
        </div>
    </div>
</div>

