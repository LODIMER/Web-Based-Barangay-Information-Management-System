<?php

declare(strict_types=1);

namespace App\Controllers;

use App\Core\Controller;
use App\Models\AyudaRequest;
use App\Models\Schedule;

class AyudaRequestController extends Controller
{
    /**
     * Show the "New Assistance Request" form (Request Ayuda).
     * This is currently a UI-only page; saving to the database
     * can be wired later.
     */
    public function create(): void
    {
        if (session_status() === PHP_SESSION_NONE) {
            session_start();
        }

        if (empty($_SESSION['user_id'])) {
            // Only logged-in residents/officials may access this page.
            $this->redirect('/login');
        }

        $role = $_SESSION['role'] ?? 'resident';

        $this->view('ayuda/request_form', [
            'title' => 'Request Ayuda',
            'role' => $role,
            'errors' => [],
            'success' => $_SESSION['ayuda_success'] ?? null,
            'old' => $_SESSION['ayuda_old'] ?? [],
        ]);

        unset($_SESSION['ayuda_success'], $_SESSION['ayuda_old']);
    }

    public function store(): void
    {
        if (session_status() === PHP_SESSION_NONE) {
            session_start();
        }

        if (empty($_SESSION['user_id'])) {
            $this->redirect('/login');
        }

        $role = $_SESSION['role'] ?? 'resident';

        $requestType = trim($_POST['request_type'] ?? '');
        $urgency = $_POST['urgency'] ?? 'medium';
        $description = trim($_POST['description'] ?? '');
        $preferredDate = $_POST['preferred_date'] ?: null;

        $errors = [];

        if ($requestType === '') {
            $errors[] = 'Request type is required.';
        }
        if ($description === '') {
            $errors[] = 'Description of need is required.';
        }

        if (!in_array($urgency, ['low', 'medium', 'high'], true)) {
            $urgency = 'medium';
        }

        $old = [
            'request_type' => $requestType,
            'urgency' => $urgency,
            'description' => $description,
            'preferred_date' => $preferredDate,
        ];

        if (!empty($errors)) {
            $this->view('ayuda/request_form', [
                'title' => 'Request Ayuda',
                'role' => $role,
                'errors' => $errors,
                'success' => null,
                'old' => $old,
            ]);
            return;
        }

        $ayudaModel = new AyudaRequest();
        $scheduleModel = new Schedule();

        $requestId = $ayudaModel->createRequest(
            $requestType,
            $urgency,
            $description,
            $preferredDate,
            '',
            (int) $_SESSION['user_id']
        );

        // When a barangay official/admin creates an ayuda and sets a preferred date,
        // automatically sync it into the Schedule.
        if (($role === 'official' || $role === 'admin') && $preferredDate !== null && $preferredDate !== '') {
            $scheduleModel->createForRequest($requestId, $preferredDate);
        }

        $_SESSION['ayuda_success'] = ($role === 'official' || $role === 'admin')
            ? 'Ayuda request and schedule saved.'
            : 'Ayuda request submitted.';

        $_SESSION['ayuda_old'] = [];

        $this->redirect('/ayuda/request');
    }
}

