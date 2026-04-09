package com.barangay.bims.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "blotter_reports")
public class BlotterReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "incident_date", nullable = false)
    private LocalDate incidentDate;

    @Column(nullable = false, length = 120)
    private String type;

    @Column(nullable = false, length = 180)
    private String location;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String details;

    @Column(name = "resident_update_request", columnDefinition = "TEXT")
    private String residentUpdateRequest;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by")
    private User reportedBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public LocalDate getIncidentDate() { return incidentDate; }
    public void setIncidentDate(LocalDate incidentDate) { this.incidentDate = incidentDate; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getResidentUpdateRequest() { return residentUpdateRequest; }
    public void setResidentUpdateRequest(String residentUpdateRequest) { this.residentUpdateRequest = residentUpdateRequest; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public User getReportedBy() { return reportedBy; }
    public void setReportedBy(User reportedBy) { this.reportedBy = reportedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

