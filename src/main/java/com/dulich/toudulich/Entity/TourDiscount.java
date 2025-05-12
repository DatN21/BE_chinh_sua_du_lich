package com.dulich.toudulich.Entity;

import com.dulich.toudulich.enums.TourDiscountStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tour_discount")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "tour_schedule_id", nullable = false)
    int tourScheduleId;

    @Column(name = "discount_type")
    String discountType;

    @Column(name = "discount_value", nullable = false)
    BigDecimal discountValue;

    @Column(name = "min_guests")
    int minGuests;

    @Column(name = "description")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    TourDiscountStatus status;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
