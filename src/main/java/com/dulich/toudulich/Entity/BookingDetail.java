package com.dulich.toudulich.Entity;

import com.dulich.toudulich.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "booking_detail")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "booking_id", nullable = false)
    int bookingId;

    @Column(name = "price_per_person", nullable = false)
    BigDecimal pricePerPerson;

    @Column(name = "full_name")
    String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;

    @Column(name = "birth_date", nullable = false)
    LocalDateTime birthDate;

    @Column(name = "age_group_id", nullable = false)
    int ageGroupId;
}
