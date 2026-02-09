<?php

declare(strict_types=1);

namespace App\Models;

use App\Core\Model;

class AyudaRequest extends Model
{
    protected string $table = 'ayuda_requests';

    public function createRequest(
        string $requestType,
        string $urgency,
        string $description,
        ?string $preferredDate,
        string $contactNumber,
        ?int $createdBy
    ): int {
        $stmt = $this->db->prepare(
            "INSERT INTO {$this->table} 
                (request_type, urgency_level, description, preferred_date, contact_number, created_by)
             VALUES (:request_type, :urgency_level, :description, :preferred_date, :contact_number, :created_by)"
        );

        $stmt->execute([
            'request_type' => $requestType,
            'urgency_level' => $urgency,
            'description' => $description,
            'preferred_date' => $preferredDate,
            'contact_number' => $contactNumber,
            'created_by' => $createdBy,
        ]);

        return (int) $this->db->lastInsertId();
    }
}

