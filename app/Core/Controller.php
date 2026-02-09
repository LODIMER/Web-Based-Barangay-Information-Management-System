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

    /**
     * Base path helper so redirects and links work when the app
     * is installed in a subdirectory under htdocs.
     */
    protected function basePath(): string
    {
        $scriptName = $_SERVER['SCRIPT_NAME'] ?? '';
        $basePath = rtrim(str_replace('\\', '/', dirname($scriptName)), '/');
        return ($basePath === '' || $basePath === '/') ? '' : $basePath;
    }

    /**
     * Convenience redirect helper that respects the base path.
     *
     * @param string $path Path beginning with "/", e.g. "/login"
     */
    protected function redirect(string $path): void
    {
        $base = $this->basePath();
        header('Location: ' . $base . $path);
        exit;
    }
}

