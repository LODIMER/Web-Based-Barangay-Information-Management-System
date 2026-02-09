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

    public function findById(int $id): ?array
    {
        $stmt = $this->db->prepare("SELECT * FROM {$this->table} WHERE id = :id LIMIT 1");
        $stmt->execute(['id' => $id]);
        $result = $stmt->fetch();
        return $result ?: null;
    }

    public function createUser(string $username, string $password, string $fullName, string $role = 'resident'): bool
    {
        $hash = password_hash($password, PASSWORD_DEFAULT);
        $stmt = $this->db->prepare(
            "INSERT INTO {$this->table} (username, password, full_name, role) VALUES (:username, :password, :full_name, :role)"
        );

        return $stmt->execute([
            'username' => $username,
            'password' => $hash,
            'full_name' => $fullName,
            'role' => $role,
        ]);
    }

    public function updateProfile(int $id, string $fullName, string $residentNumber, string $contactNumber, string $address, ?string $idDocumentPath): bool
    {
        $fields = [
            'full_name' => $fullName,
            'resident_number' => $residentNumber,
            'contact_number' => $contactNumber,
            'address' => $address,
        ];

        if ($idDocumentPath !== null) {
            $fields['id_document_path'] = $idDocumentPath;
        }

        $setParts = [];
        $params = ['id' => $id];

        foreach ($fields as $column => $value) {
            $setParts[] = "{$column} = :{$column}";
            $params[$column] = $value;
        }

        $sql = "UPDATE {$this->table} SET " . implode(', ', $setParts) . " WHERE id = :id";
        $stmt = $this->db->prepare($sql);
        return $stmt->execute($params);
    }
}

