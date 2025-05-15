package com.dulich.toudulich.responses;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourByAgeResponse {
    int id;

    String describe;

    BigDecimal priceRate;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
    String key;
    String header;
    public static TourByAgeResponse fromTourPriceByAge(int id,
                                                        String describe,
                                                        BigDecimal priceRate,
                                                        LocalDateTime createdAt,
                                                        LocalDateTime updatedAt,
                                                        String key,
                                                        String header) {
        return TourByAgeResponse.builder()
                .id(id)
                .describe(describe)
                .priceRate(priceRate)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .key(key)
                .header(header)
                .build();
    }
}
