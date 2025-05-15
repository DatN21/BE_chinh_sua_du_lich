package com.dulich.toudulich.Entity;

import com.dulich.toudulich.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "booking")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "customer_id", nullable = false)
    int customerId;

    @Column(name = "tour_schedule_id", nullable = false)
    int tourScheduleId;

    @Column(name = "booked_slots", nullable = false)
    int bookedSlots;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    BookingStatus status;

    @Column(name = "note")
    String note;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(name = "phone", nullable = false)
    String phone;

    @Column(name = "email")
    String email;
    @Column(name = "address")
    String address;
}
