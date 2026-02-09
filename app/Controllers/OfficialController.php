<?php

declare(strict_types=1);

namespace App\Controllers;

use App\Core\Controller;
use App\Models\Official;

class OfficialController extends Controller
{
    public function index(): void
    {
        $officialModel = new Official();

        $this->view('officials/index', [
            'title' => 'Barangay Officials',
            'officials' => $officialModel->all(),
        ]);
    }
}

