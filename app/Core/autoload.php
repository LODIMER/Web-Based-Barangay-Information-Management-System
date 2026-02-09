<?php

declare(strict_types=1);

/**
 * Simple PSR-4 style autoloader for the App namespace.
 * This replaces Composer's autoloader so you don't need vendor/autoload.php.
 */
spl_autoload_register(static function (string $class): void {
    $prefix  = 'App\\';
    $baseDir = __DIR__ . '/../'; // points to app/

    $len = strlen($prefix);
    if (strncmp($prefix, $class, $len) !== 0) {
        // Not part of the App namespace; ignore.
        return;
    }

    $relativeClass = substr($class, $len);
    $file = $baseDir . str_replace('\\', DIRECTORY_SEPARATOR, $relativeClass) . '.php';

    if (is_file($file)) {
        require $file;
    }
});

