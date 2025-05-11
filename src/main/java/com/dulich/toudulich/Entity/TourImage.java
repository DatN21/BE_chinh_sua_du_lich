package com.dulich.toudulich.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tour_image")
public class TourImage {
    public static final int
            MAXIMUM_IMAGE_P_PRODUCT = 10;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "tour_id", nullable = false)
    int tourId;

    @Column(name = "image_url", nullable = false)
    String imageUrl;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}
