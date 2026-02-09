<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

// Determine base path when app is installed in a subdirectory under htdocs.
$scriptName = $_SERVER['SCRIPT_NAME'] ?? '';
$basePath = rtrim(str_replace('\\', '/', dirname($scriptName)), '/');
$baseUrl = ($basePath === '' || $basePath === '/') ? '' : $basePath;
// Public assets (css/js) live under /public
$assetBase = $baseUrl . '/public';

$bodyClass = isset($bodyClass) ? htmlspecialchars($bodyClass) : 'bg-light';
$isAuthLayout = ($bodyClass === 'bg-auth');
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title><?= isset($title) ? htmlspecialchars($title) . ' | ' : '' ?>Barangay Information System</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<?= $assetBase ?>/css/style.css" rel="stylesheet">
</head>
<body class="<?= $bodyClass ?>">
    <?php if (empty($hideNavbar) && !$isAuthLayout): ?>
        <?php include __DIR__ . '/../partials/navbar.php'; ?>
    <?php endif; ?>

    <?php if ($isAuthLayout): ?>
        <main class="auth-shell">
            <?php include $viewFile; ?>
        </main>
    <?php else: ?>
        <main class="container py-4">
            <?php include $viewFile; ?>
        </main>
    <?php endif; ?>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="<?= $assetBase ?>/js/app.js" defer></script>
</body>
</html>

