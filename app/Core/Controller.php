<?php

declare(strict_types=1);

namespace App\Core;

class Controller
{
    protected function view(string $template, array $data = []): void
    {
        extract($data);
        $viewFile = __DIR__ . '/../Views/' . $template . '.php';

        if (!file_exists($viewFile)) {
            http_response_code(500);
            echo "View {$template} not found.";
            return;
        }

        include __DIR__ . '/../Views/layouts/main.php';
    }
}

