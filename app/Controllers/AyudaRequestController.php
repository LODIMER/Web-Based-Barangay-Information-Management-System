<?php

declare(strict_types=1);

namespace App\Controllers;

use App\Core\Controller;

class AyudaRequestController extends Controller
{
    /**
     * Show the "New Assistance Request" form (Request Ayuda).
     * This is currently a UI-only page; saving to the database
     * can be wired later.
     */
    public function create(): void
    {
        $this->view('ayuda/request_form', [
            'title' => 'Request Ayuda',
        ]);
    }
}

