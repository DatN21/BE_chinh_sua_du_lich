package com.dulich.toudulich.DTO;

import com.dulich.toudulich.Entity.BookingDetail;
import com.dulich.toudulich.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate birthDate;
    int ageGroupId;
    BigDecimal pricePerPerson;
    String phone;

    public BookingDetailDTO(int id, int bookingId, float tourScheduleId, String fullName, Gender gender, LocalDate birthDate, int ageGroupId, BigDecimal pricePerPerson) {
        this.id = id;
        this.bookingId = bookingId;
        this.tourScheduleId = tourScheduleId;
        this.fullName = fullName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.ageGroupId = ageGroupId;
        this.pricePerPerson = pricePerPerson;
    }

    public BookingDetailDTO(String fullName, BigDecimal pricePerPerson, Gender gender, LocalDateTime birthDate, int ageGroupId) {
        // viết tiếp
        this.fullName = fullName;
        this.pricePerPerson = pricePerPerson;
        this.gender = gender;
        this.birthDate = birthDate.toLocalDate();
        this.ageGroupId = ageGroupId;
    }
}