package com.dulich.toudulich.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotNull(message = "UserId can't be empty")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    UserModel userModel;

    @NotEmpty(message = "Full name can't be empty")
    @Column(name = "full_name")
    String fullName;

    @NotEmpty(message = "Phone number can't be empty")
    @Column(name = "phone_number")
    String phoneNumber;

    @NotNull(message = "RoleId can't be empty")
    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    TourModel tourId;

    @NotEmpty(message = "Tour name can't be empty")
    @Column(name = "tour_name", nullable = false, length = 255)
    String tourName;

    @Column(name = "amount", nullable = false)
    float amount;

    @Column(name = "start_date", nullable = false)
    Date startDate;

    @Column(name = "total_price", nullable = false)
    float totalPrice;

    @Column(name = "status", length = 50)
    String status;


    @Column(name = "notes")
    String notes;

    @Column(name = "booking_time")
    LocalDateTime bookingTime;

    @PrePersist
    protected void onCreate() {
        bookingTime = LocalDateTime.now();
    }
}
