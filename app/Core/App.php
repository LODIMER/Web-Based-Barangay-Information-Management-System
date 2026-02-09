<?php

declare(strict_types=1);

namespace App\Core;

class App
{
    public function run(): void
    {
        $router = new Router();

        // Define routes
        $router->get('/', [\App\Controllers\DashboardController::class, 'index']);
        $router->get('/residents', [\App\Controllers\ResidentController::class, 'index']);
        $router->get('/households', [\App\Controllers\HouseholdController::class, 'index']);
        $router->get('/officials', [\App\Controllers\OfficialController::class, 'index']);
        $router->get('/ayuda/request', [\App\Controllers\AyudaRequestController::class, 'create']);
        $router->get('/login', [\App\Controllers\AuthController::class, 'loginForm']);
        $router->post('/login', [\App\Controllers\AuthController::class, 'login']);
        $router->get('/register', [\App\Controllers\AuthController::class, 'registerForm']);
        $router->post('/register', [\App\Controllers\AuthController::class, 'register']);
        $router->get('/logout', [\App\Controllers\AuthController::class, 'logout']);

        $method = $_SERVER['REQUEST_METHOD'] ?? 'GET';
        $uri = $_SERVER['REQUEST_URI'] ?? '/';

        // Make routing work when the app is installed in a subdirectory,
        // e.g. /Web-Based-Barangay-Information-Management-System/.
        $path = parse_url($uri, PHP_URL_PATH) ?? '/';
        $scriptName = $_SERVER['SCRIPT_NAME'] ?? '';
        $basePath = str_replace('\\', '/', dirname($scriptName));
        $basePath = rtrim($basePath, '/');

        if ($basePath !== '' && $basePath !== '/' && str_starts_with($path, $basePath)) {
            $path = substr($path, strlen($basePath)) ?: '/';
        }

        $router->dispatch($method, $path);
    }
}

