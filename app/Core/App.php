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
        $router->get('/login', [\App\Controllers\AuthController::class, 'loginForm']);
        $router->post('/login', [\App\Controllers\AuthController::class, 'login']);
        $router->get('/logout', [\App\Controllers\AuthController::class, 'logout']);

        $router->dispatch($_SERVER['REQUEST_METHOD'] ?? 'GET', $_SERVER['REQUEST_URI'] ?? '/');
    }
}

