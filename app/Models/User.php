<?php

declare(strict_types=1);

namespace App\Models;

use App\Core\Model;

class User extends Model
{
    protected string $table = 'users';

    public function findByUsername(string $username): ?array
    {
        $stmt = $this->db->prepare("SELECT * FROM {$this->table} WHERE username = :username LIMIT 1");
        $stmt->execute(['username' => $username]);
        $result = $stmt->fetch();
        return $result ?: null;
    }

    public function createUser(string $username, string $password, string $fullName): bool
    {
        $hash = password_hash($password, PASSWORD_DEFAULT);
        $stmt = $this->db->prepare(
            "INSERT INTO {$this->table} (username, password, full_name) VALUES (:username, :password, :full_name)"
        );

        return $stmt->execute([
            'username' => $username,
            'password' => $hash,
            'full_name' => $fullName,
        ]);
    }
}

