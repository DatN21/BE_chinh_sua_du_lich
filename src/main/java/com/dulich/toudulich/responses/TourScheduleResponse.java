package com.dulich.toudulich.responses;

import com.dulich.toudulich.enums.TourScheduleStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourScheduleResponse {
    int id;
    int tourId;

    LocalDateTime startDate;

    LocalDateTime endDate;

    int totalSlots;

    int availableSlots;

    int bookedSlots;

    String status;

    public static TourScheduleResponse TourScheduleResponseMapper(int id,
                                                                int tourId,
                                                                 LocalDateTime startDate,
                                                                 LocalDateTime endDate,
                                                                 int totalSlots,
                                                                 int availableSlots,
                                                                 int bookedSlots,
                                                                 String status) {
        return TourScheduleResponse.builder()
                .id(id)
                .tourId(tourId)
                .startDate(startDate)
                .endDate(endDate)
                .totalSlots(totalSlots)
                .availableSlots(availableSlots)
                .bookedSlots(bookedSlots)
                .status(status)
                .build();
    }
}
