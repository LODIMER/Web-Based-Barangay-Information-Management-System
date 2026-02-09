<?php

declare(strict_types=1);

namespace App\Controllers;

use App\Core\Controller;

class ScheduleController extends Controller
{
    public function index(): void
    {
        if (session_status() === PHP_SESSION_NONE) {
            session_start();
        }

        if (empty($_SESSION['user_id'])) {
            $this->redirect('/login');
        }

        // Demo data for now; can be loaded from DB later.
        $upcoming = [];

        $this->view('schedule/index', [
            'title' => 'My Schedule',
            'upcoming' => $upcoming,
        ]);
    }
}

