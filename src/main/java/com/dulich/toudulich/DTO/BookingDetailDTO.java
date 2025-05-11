package com.dulich.toudulich.DTO;

import com.dulich.toudulich.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDetailDTO {
    int id;
    int bookingId;
    float tourScheduleId;
    String fullName;
    Gender gender;
    String birthDate;
    int ageGroupId;
}