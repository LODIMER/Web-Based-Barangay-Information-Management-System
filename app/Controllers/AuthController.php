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
            'success' => $_SESSION['success'] ?? null,
            'bodyClass' => 'bg-auth',
            'hideNavbar' => true,
        ]);

        unset($_SESSION['success']);
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
                'success' => null,
                'bodyClass' => 'bg-auth',
                'hideNavbar' => true,
            ]);
            return;
        }

        $_SESSION['user_id'] = $user['id'];
        $_SESSION['username'] = $user['username'];

        header('Location: ' . $this->basePath() . '/');
        exit;
    }

    public function registerForm(): void
    {
        $this->view('auth/register', [
            'title' => 'Register',
            'errors' => [],
            'old' => [
                'username' => '',
                'full_name' => '',
            ],
            'bodyClass' => 'bg-auth',
            'hideNavbar' => true,
        ]);
    }

    public function register(): void
    {
        $username = trim($_POST['username'] ?? '');
        $fullName = trim($_POST['full_name'] ?? '');
        $password = $_POST['password'] ?? '';
        $passwordConfirm = $_POST['password_confirm'] ?? '';

        $errors = [];

        if ($username === '') {
            $errors[] = 'Username is required.';
        }

        if ($fullName === '') {
            $errors[] = 'Full name is required.';
        }

        if ($password === '') {
            $errors[] = 'Password is required.';
        } elseif (strlen($password) < 6) {
            $errors[] = 'Password must be at least 6 characters.';
        }

        if ($password !== $passwordConfirm) {
            $errors[] = 'Passwords do not match.';
        }

        $userModel = new User();

        if ($username !== '' && $userModel->findByUsername($username)) {
            $errors[] = 'Username is already taken.';
        }

        if (!empty($errors)) {
            $this->view('auth/register', [
                'title' => 'Register',
                'errors' => $errors,
                'old' => [
                    'username' => $username,
                    'full_name' => $fullName,
                ],
                'bodyClass' => 'bg-auth',
                'hideNavbar' => true,
            ]);
            return;
        }

        if (!$userModel->createUser($username, $password, $fullName)) {
            $this->view('auth/register', [
                'title' => 'Register',
                'errors' => ['Failed to create account. Please try again.'],
                'old' => [
                    'username' => $username,
                    'full_name' => $fullName,
                ],
                'bodyClass' => 'bg-auth',
                'hideNavbar' => true,
            ]);
            return;
        }

        $_SESSION['success'] = 'Account created. You can now log in.';
        header('Location: ' . $this->basePath() . '/login');
        exit;
    }

    public function logout(): void
    {
        session_start();
        session_destroy();
        header('Location: ' . $this->basePath() . '/login');
        exit;
    }

    private function basePath(): string
    {
        $scriptName = $_SERVER['SCRIPT_NAME'] ?? '';
        $basePath = rtrim(str_replace('\\', '/', dirname($scriptName)), '/');
        return ($basePath === '' || $basePath === '/') ? '' : $basePath;
    }
}

