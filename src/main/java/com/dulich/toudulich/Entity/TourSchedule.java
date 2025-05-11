package com.dulich.toudulich.Entity;

import com.dulich.toudulich.enums.TourScheduleStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tour_schedule")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "tour_id", nullable = false)
    int tourId;

    @Column(name = "start_date", nullable = false)
    String startDate;

    @Column(name = "end_date", nullable = false)
    String endDate;

    @Column(name = "total_slots", nullable = false)
    int totalSlots;

    @Column(name = "available_slots", nullable = false)
    int availableSlots;

    @Column(name = "booked_slots")
    int bookedSlots;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "status", nullable = false)
    TourScheduleStatus status;
}
