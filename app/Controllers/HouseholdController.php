<?php

declare(strict_types=1);

namespace App\Controllers;

use App\Core\Controller;
use App\Models\Household;

class HouseholdController extends Controller
{
    public function index(): void
    {
        $householdModel = new Household();

        $this->view('households/index', [
            'title' => 'Households',
            'households' => $householdModel->all(),
        ]);
    }
}

