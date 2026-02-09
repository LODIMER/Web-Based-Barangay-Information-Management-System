<div class="row justify-content-center">
    <div class="col-lg-8 col-xl-7">
        <div class="card shadow-sm border-0">
            <div class="card-body p-4">
                <?php $role = $role ?? 'resident'; ?>
                <h1 class="h4 mb-1">
                    <?= ($role === 'official' || $role === 'admin') ? 'Add Ayuda Schedule' : 'Request Ayuda' ?>
                </h1>
                <p class="text-muted mb-3 small">
                    <?= ($role === 'official' || $role === 'admin')
                        ? 'Create a new ayuda request and assign a schedule.'
                        : 'Fill out the form below to request assistance from the barangay.' ?>
                </p>

                <?php if (!empty($success)): ?>
                    <div class="alert alert-success py-2 small"><?= htmlspecialchars($success) ?></div>
                <?php endif; ?>

                <?php if (!empty($errors)): ?>
                    <div class="alert alert-danger py-2 small">
                        <ul class="mb-0">
                            <?php foreach ($errors as $message): ?>
                                <li><?= htmlspecialchars($message) ?></li>
                            <?php endforeach; ?>
                        </ul>
                    </div>
                <?php endif; ?>

                <form method="post" action="<?= $baseUrl ?>/ayuda/request">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label for="request_type" class="form-label mb-1 small fw-semibold text-muted text-uppercase">
                                Request Type *
                            </label>
                            <select id="request_type" name="request_type" class="form-select" required>
                                <option value="" disabled <?= empty($old['request_type'] ?? '') ? 'selected' : '' ?>>
                                    Select type of assistance
                                </option>
                                <?php
                                $types = [
                                    'Financial Assistance',
                                    'Medical / Health',
                                    'Food Packs / Relief Goods',
                                    'Document Processing',
                                    'Legal Advice',
                                    'Other',
                                ];
                                foreach ($types as $type):
                                    $selected = (($old['request_type'] ?? '') === $type) ? 'selected' : '';
                                ?>
                                    <option <?= $selected ?>><?= $type ?></option>
                                <?php endforeach; ?>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label for="urgency" class="form-label mb-1 small fw-semibold text-muted text-uppercase">
                                Urgency Level *
                            </label>
                            <select id="urgency" name="urgency" class="form-select">
                                <?php
                                $urgency = $old['urgency'] ?? 'medium';
                                ?>
                                <option value="low" <?= $urgency === 'low' ? 'selected' : '' ?>>Low - Can wait</option>
                                <option value="medium" <?= $urgency === 'medium' ? 'selected' : '' ?>>Medium - Needed soon</option>
                                <option value="high" <?= $urgency === 'high' ? 'selected' : '' ?>>High - Urgent</option>
                            </select>
                        </div>
                        <div class="col-12">
                            <label for="description" class="form-label mb-1 small fw-semibold text-muted text-uppercase">
                                Description of Need *
                            </label>
                            <textarea
                                id="description"
                                name="description"
                                class="form-control"
                                rows="4"
                                placeholder="Please describe the situation and what specific help is needed..."
                                required
                            ><?= htmlspecialchars($old['description'] ?? '') ?></textarea>
                        </div>
                        <div class="col-md-6">
                            <label for="preferred_date" class="form-label mb-1 small fw-semibold text-muted text-uppercase">
                                Preferred Date
                            </label>
                            <input
                                type="date"
                                id="preferred_date"
                                name="preferred_date"
                                class="form-control"
                                value="<?= htmlspecialchars($old['preferred_date'] ?? '') ?>"
                            >
                            <div class="form-text small">
                                Optional preference for when help can be given.
                            </div>
                        </div>
                    </div>

                    <div class="alert alert-primary mt-4 mb-3 small">
                        <strong>Important Reminder:</strong>
                        <ul class="mb-0 ps-3 mt-1">
                            <li>All requests are subject to evaluation and approval.</li>
                            <li>You may be contacted for further interview or validation.</li>
                            <li>Providing false information may lead to disqualification.</li>
                        </ul>
                    </div>

                    <button type="submit" class="btn btn-success w-100 fw-semibold">
                        <?= ($role === 'official' || $role === 'admin') ? 'Save Ayuda & Schedule' : 'Submit Request' ?>
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

