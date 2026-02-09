<?php

declare(strict_types=1);

use App\Core\App;

// Register a lightweight autoloader for the App\ namespace
require_once __DIR__ . '/../app/Core/autoload.php';

// Front controller
$app = new App();
$app->run();

