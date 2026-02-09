<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h4 mb-0">Barangay Officials</h1>
    <button class="btn btn-primary btn-sm" disabled>+ Add Official (demo)</button>
</div>

<div class="card shadow-sm border-0">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-light">
                <tr>
                    <th>#</th>
                    <th>Full Name</th>
                    <th>Position</th>
                    <th>Term Start</th>
                    <th>Term End</th>
                </tr>
                </thead>
                <tbody>
                <?php if (!empty($officials)): ?>
                    <?php foreach ($officials as $official): ?>
                        <tr>
                            <td><?= (int) $official['id'] ?></td>
                            <td><?= htmlspecialchars($official['name']) ?></td>
                            <td><?= htmlspecialchars($official['position']) ?></td>
                            <td><?= htmlspecialchars($official['term_start']) ?></td>
                            <td><?= htmlspecialchars($official['term_end']) ?></td>
                        </tr>
                    <?php endforeach; ?>
                <?php else: ?>
                    <tr>
                        <td colspan="5" class="text-center py-4 text-muted">
                            No officials found. Seed the database to see data.
                        </td>
                    </tr>
                <?php endif; ?>
                </tbody>
            </table>
        </div>
    </div>
</div>

