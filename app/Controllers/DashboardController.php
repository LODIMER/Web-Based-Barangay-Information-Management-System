<?php

declare(strict_types=1);

namespace App\Controllers;

use App\Core\Controller;
use App\Models\Resident;
use App\Models\Household;
use App\Models\Official;

class DashboardController extends Controller
{
    public function index(): void
    {
        if (session_status() === PHP_SESSION_NONE) {
            session_start();
        }

        $role = $_SESSION['role'] ?? 'resident';

        $residentModel = new Resident();
        $householdModel = new Household();
        $officialModel  = new Official();

        $this->view('dashboard/index', [
            'title' => 'Dashboard',
            'residentCount' => $residentModel->count(),
            'householdCount' => $householdModel->count(),
            'officialCount' => $officialModel->count(),
            'role' => $role,
            // Placeholder upcoming schedule items for residents; can be loaded from DB later.
            'upcoming' => [],
        ]);
    }
}

