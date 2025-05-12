package com.dulich.toudulich.DTO;

import com.dulich.toudulich.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BookingDetailDTO {
    int id;
    int bookingId;
    float tourScheduleId;
    String fullName;
    Gender gender;
    LocalDateTime birthDate;
    int ageGroupId;
    BigDecimal pricePerPerson;
}