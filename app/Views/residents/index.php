<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h4 mb-0">Residents</h1>
    <button class="btn btn-primary btn-sm" disabled>+ Add Resident (demo)</button>
</div>

<div class="card shadow-sm border-0">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-light">
                <tr>
                    <th>#</th>
                    <th>Full Name</th>
                    <th>Sex</th>
                    <th>Date of Birth</th>
                    <th>Address</th>
                </tr>
                </thead>
                <tbody>
                <?php if (!empty($residents)): ?>
                    <?php foreach ($residents as $resident): ?>
                        <tr>
                            <td><?= (int) $resident['id'] ?></td>
                            <td><?= htmlspecialchars($resident['last_name'] . ', ' . $resident['first_name']) ?></td>
                            <td><?= htmlspecialchars($resident['sex']) ?></td>
                            <td><?= htmlspecialchars($resident['birth_date']) ?></td>
                            <td><?= htmlspecialchars($resident['address']) ?></td>
                        </tr>
                    <?php endforeach; ?>
                <?php else: ?>
                    <tr>
                        <td colspan="5" class="text-center py-4 text-muted">
                            No residents found. Seed the database to see data.
                        </td>
                    </tr>
                <?php endif; ?>
                </tbody>
            </table>
        </div>
    </div>
</div>

