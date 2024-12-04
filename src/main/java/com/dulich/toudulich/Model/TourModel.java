package com.dulich.toudulich.Model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import  com.dulich.toudulich.enums.TourType;
import  com.dulich.toudulich.enums.Status;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tours")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "tour_name", nullable = false, length = 255)
    String tourName;

    @Column(name = "days", nullable = false)
    String days;

    @Column(name = "start_date", nullable = false)
    String startDate;

    @Column(name = "destination", nullable = false, length = 255)
    String destination;

    @Column(name = "tour_type")
    @Enumerated(EnumType.STRING)
    TourType tourType;

    @Column(name = "departure_location", nullable = false, length = 255)
    String departureLocation;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;

    @Column(name = "price", nullable = false)
    float price;

//    @Column(name = "thumbnail")
//    String thubnail ;

    @Column(name = "description")
    String description ;

    @Column(name = "content",columnDefinition = "LONGTEXT")
    String content ;

    @Column(name = "image_header", columnDefinition = "LONGTEXT")
    String imageHeader ;
}
