<?php

declare(strict_types=1);

namespace App\Controllers;

use App\Core\Controller;
use App\Models\User;

class AuthController extends Controller
{
    public function loginForm(): void
    {
        $this->view('auth/login', [
            'title' => 'Login',
            'error' => null,
        ]);
    }

    public function login(): void
    {
        session_start();

        $username = $_POST['username'] ?? '';
        $password = $_POST['password'] ?? '';

        $userModel = new User();
        $user = $userModel->findByUsername($username);

        if (!$user || !password_verify($password, $user['password'] ?? '')) {
            $this->view('auth/login', [
                'title' => 'Login',
                'error' => 'Invalid credentials.',
            ]);
            return;
        }

        $_SESSION['user_id'] = $user['id'];
        $_SESSION['username'] = $user['username'];

        header('Location: /');
        exit;
    }

    public function logout(): void
    {
        session_start();
        session_destroy();
        header('Location: /login');
        exit;
    }
}

