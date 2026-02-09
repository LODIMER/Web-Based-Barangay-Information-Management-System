<?php

declare(strict_types=1);

use App\Core\App;

require_once __DIR__ . '/../vendor/autoload.php';

// Front controller
$app = new App();
$app->run();

