<?php

declare(strict_types=1);

namespace App\Controllers;

use App\Core\Controller;
use App\Models\Resident;

class ResidentController extends Controller
{
    public function index(): void
    {
        $residentModel = new Resident();

        $this->view('residents/index', [
            'title' => 'Residents',
            'residents' => $residentModel->all(),
        ]);
    }
}

