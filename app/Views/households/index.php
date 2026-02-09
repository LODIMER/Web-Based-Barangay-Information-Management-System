<div class="d-flex justify-content-between align-items-center mb-3">
    <h1 class="h4 mb-0">Households</h1>
    <button class="btn btn-primary btn-sm" disabled>+ Add Household (demo)</button>
</div>

<div class="card shadow-sm border-0">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-light">
                <tr>
                    <th>#</th>
                    <th>Household Head</th>
                    <th>Address</th>
                    <th>Total Members</th>
                </tr>
                </thead>
                <tbody>
                <?php if (!empty($households)): ?>
                    <?php foreach ($households as $household): ?>
                        <tr>
                            <td><?= (int) $household['id'] ?></td>
                            <td><?= htmlspecialchars($household['head_name']) ?></td>
                            <td><?= htmlspecialchars($household['address']) ?></td>
                            <td><?= (int) ($household['members_count'] ?? 0) ?></td>
                        </tr>
                    <?php endforeach; ?>
                <?php else: ?>
                    <tr>
                        <td colspan="4" class="text-center py-4 text-muted">
                            No households found. Seed the database to see data.
                        </td>
                    </tr>
                <?php endif; ?>
                </tbody>
            </table>
        </div>
    </div>
</div>

