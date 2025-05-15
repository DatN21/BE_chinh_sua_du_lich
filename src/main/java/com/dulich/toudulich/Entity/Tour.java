package com.dulich.toudulich.Entity;

import com.dulich.toudulich.enums.TourStatus;
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
@Table(name = "tour")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(unique = true, nullable = false)
    private String code;  // Mã tour

    @Column(name = "name", nullable = false, length = 255)
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "price", nullable = false)
    BigDecimal price;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TourStatus status;

    @Column(name = "duration", nullable = false)
    int duration;

    @Column(name = "depature_location", nullable = false)
    String depatureLocation;

    @Column(name = "image_header", nullable = false)
    String imageHeader;
}
