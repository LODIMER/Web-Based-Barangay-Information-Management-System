<?php

declare(strict_types=1);

namespace App\Controllers;

use App\Core\Controller;
use App\Models\Schedule;

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

        $scheduleModel = new Schedule();
        $rows = $scheduleModel->upcomingWithDetails();

        $upcoming = array_map(static function (array $row): array {
            return [
                'title' => $row['request_type'],
                'subtitle' => $row['scheduled_date'],
                'status' => ucfirst($row['urgency_level']),
            ];
        }, $rows);

        $this->view('schedule/index', [
            'title' => 'My Schedule',
            'upcoming' => $upcoming,
        ]);
    }
}

