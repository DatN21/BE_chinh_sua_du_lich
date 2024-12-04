package com.dulich.toudulich.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tour_image")
public class TourImageModel {
    public static final int MAXIMUM_IMAGE_P_PRODUCT = 10;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @JoinColumn(name = "tour_id", nullable = false)
    @ManyToOne
    private TourModel tourModel ;

    @Column(name = "image_url", nullable = false,length = 300)
    private String imageUrl ;
}
