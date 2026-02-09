<?php
/** @var array $user */
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
$role = $_SESSION['role'] ?? 'resident';
$residentNumber = $user['resident_number'] ?? '';
$contact = $user['contact_number'] ?? '';
$address = $user['address'] ?? '';
$idPath = $user['id_document_path'] ?? null;
$isVerified = !empty($user['is_verified']);
?>

<div class="row justify-content-center">
    <div class="col-lg-8 col-xl-7">
        <div class="card shadow-sm border-0 mb-3">
            <div class="card-body p-4">
                <h1 class="h5 mb-3">My Profile</h1>

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

                <form method="post" action="<?= $baseUrl ?>/profile" enctype="multipart/form-data" class="small">
                    <div class="mb-3">
                        <label for="full_name" class="form-label fw-semibold">Full Name</label>
                        <input
                            type="text"
                            id="full_name"
                            name="full_name"
                            class="form-control"
                            required
                            value="<?= htmlspecialchars($user['full_name'] ?? '') ?>"
                        >
                    </div>

                    <?php if ($role === 'resident'): ?>
                        <div class="mb-3">
                            <label for="resident_number" class="form-label fw-semibold">Resident Number</label>
                            <input
                                type="text"
                                id="resident_number"
                                name="resident_number"
                                class="form-control"
                                placeholder="e.g. RES-001"
                                value="<?= htmlspecialchars($residentNumber) ?>"
                            >
                            <div class="form-text">
                                This is your unique resident account number.
                            </div>
                        </div>
                    <?php endif; ?>

                    <div class="mb-3">
                        <label for="contact_number" class="form-label fw-semibold">Contact Number</label>
                        <input
                            type="tel"
                            id="contact_number"
                            name="contact_number"
                            class="form-control"
                            placeholder="e.g. 09171234567"
                            value="<?= htmlspecialchars($contact) ?>"
                        >
                    </div>

                    <div class="mb-3">
                        <label for="address" class="form-label fw-semibold">Address</label>
                        <textarea
                            id="address"
                            name="address"
                            class="form-control"
                            rows="2"
                            placeholder="House No., Street, Barangay, City"
                        ><?= htmlspecialchars($address) ?></textarea>
                    </div>

                    <hr class="my-4">

                    <h2 class="h6 mb-2">Valid ID for Verification</h2>
                    <?php if ($isVerified): ?>
                        <p class="text-success small mb-2">
                            ✔ Your account is marked as verified.
                        </p>
                    <?php else: ?>
                        <p class="text-muted small mb-2">
                            Upload a clear photo or scan of a government-issued ID (JPG, PNG, or PDF).
                        </p>
                    <?php endif; ?>

                    <?php if ($idPath): ?>
                        <div class="mb-2 small">
                            <span class="text-muted">Current ID on file:</span>
                            <a href="<?= $assetBase . $idPath ?>" target="_blank">View uploaded ID</a>
                        </div>
                    <?php endif; ?>

                    <div class="mb-3">
                        <input
                            type="file"
                            name="valid_id"
                            id="valid_id"
                            class="form-control"
                            accept=".jpg,.jpeg,.png,.pdf"
                        >
                    </div>

                    <button type="submit" class="btn btn-primary">
                        Save Changes
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

