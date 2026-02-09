<?php

declare(strict_types=1);

namespace App\Models;

use App\Core\Model;

class Schedule extends Model
{
    protected string $table = 'schedules';

    public function createForRequest(int $ayudaRequestId, string $scheduledDate): bool
    {
        $stmt = $this->db->prepare(
            "INSERT INTO {$this->table} (ayuda_request_id, scheduled_date)
             VALUES (:ayuda_request_id, :scheduled_date)"
        );

        return $stmt->execute([
            'ayuda_request_id' => $ayudaRequestId,
            'scheduled_date' => $scheduledDate,
        ]);
    }

    /**
     * Return upcoming schedules joined with ayuda requests
     * (used for the sidebar list).
     */
    public function upcomingWithDetails(): array
    {
        $stmt = $this->db->query(
            "SELECT s.scheduled_date, a.request_type, a.urgency_level
             FROM {$this->table} s
             JOIN ayuda_requests a ON a.id = s.ayuda_request_id
             ORDER BY s.scheduled_date ASC
             LIMIT 10"
        );

        return $stmt->fetchAll();
    }
}

