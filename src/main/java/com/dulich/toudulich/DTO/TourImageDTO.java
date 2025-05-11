package com.dulich.toudulich.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourImageDTO {
    private Integer id ;

    @Min(value = 1, message = "Product id must be >0")
    private Integer tourId ;

    @Size(min = 5,max = 200,message = "Image's name")
    private String imgUrl ;

    public Integer getTourId() {
        return tourId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
