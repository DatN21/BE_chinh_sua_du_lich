package com.dulich.toudulich.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tour_price_by_age")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourPriceByAge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "describe")
    String describe;

    @Column(name = "price_rate")
    BigDecimal priceRate;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "key")
    String key;

    @Column(name = "header")
    String header;
}
