<?php

declare(strict_types=1);

namespace App\Controllers;

use App\Core\Controller;
use App\Models\User;

class ProfileController extends Controller
{
    public function show(): void
    {
        if (session_status() === PHP_SESSION_NONE) {
            session_start();
        }

        if (empty($_SESSION['user_id'])) {
            $this->redirect('/login');
        }

        $userModel = new User();
        $user = $userModel->findById((int) $_SESSION['user_id']);

        if (!$user) {
            $this->redirect('/logout');
        }

        $this->view('profile/index', [
            'title' => 'My Profile',
            'user' => $user,
        ]);
    }

    public function update(): void
    {
        if (session_status() === PHP_SESSION_NONE) {
            session_start();
        }

        if (empty($_SESSION['user_id'])) {
            $this->redirect('/login');
        }

        $userId = (int) $_SESSION['user_id'];
        $userModel = new User();
        $user = $userModel->findById($userId);

        if (!$user) {
            $this->redirect('/logout');
        }

        $fullName = trim($_POST['full_name'] ?? '');
        $residentNumber = trim($_POST['resident_number'] ?? ($user['resident_number'] ?? ''));
        $contact = trim($_POST['contact_number'] ?? '');
        $address = trim($_POST['address'] ?? '');

        $errors = [];

        if ($fullName === '') {
            $errors[] = 'Full name is required.';
        }

        // Handle ID upload (optional).
        $idPath = $user['id_document_path'] ?? null;
        if (!empty($_FILES['valid_id']['name'] ?? '')) {
            $uploadError = $_FILES['valid_id']['error'] ?? UPLOAD_ERR_NO_FILE;
            if ($uploadError === UPLOAD_ERR_OK) {
                $tmp = $_FILES['valid_id']['tmp_name'];
                $original = basename($_FILES['valid_id']['name']);
                $ext = strtolower(pathinfo($original, PATHINFO_EXTENSION));

                if (!in_array($ext, ['jpg', 'jpeg', 'png', 'pdf'], true)) {
                    $errors[] = 'Valid ID must be a JPG, PNG, or PDF file.';
                } else {
                    $uploadDir = dirname(__DIR__, 2) . '/public/uploads/ids';
                    if (!is_dir($uploadDir)) {
                        mkdir($uploadDir, 0777, true);
                    }
                    $fileName = 'user_' . $userId . '_' . time() . '.' . $ext;
                    $destination = $uploadDir . '/' . $fileName;

                    if (move_uploaded_file($tmp, $destination)) {
                        $relativePath = '/uploads/ids/' . $fileName;
                        $idPath = $relativePath;
                    } else {
                        $errors[] = 'Failed to upload ID file. Please try again.';
                    }
                }
            } elseif ($uploadError !== UPLOAD_ERR_NO_FILE) {
                $errors[] = 'Failed to upload ID file. Please try again.';
            }
        }

        if (!empty($errors)) {
            $this->view('profile/index', [
                'title' => 'My Profile',
                'user' => array_merge($user, [
                    'full_name' => $fullName,
                    'resident_number' => $residentNumber,
                    'contact_number' => $contact,
                    'address' => $address,
                    'id_document_path' => $idPath,
                ]),
                'errors' => $errors,
                'success' => null,
            ]);
            return;
        }

        $userModel->updateProfile($userId, $fullName, $residentNumber, $contact, $address, $idPath);

        $_SESSION['username'] = $fullName;

        $updated = $userModel->findById($userId);

        $this->view('profile/index', [
            'title' => 'My Profile',
            'user' => $updated,
            'errors' => [],
            'success' => 'Profile updated successfully.',
        ]);
    }
}

