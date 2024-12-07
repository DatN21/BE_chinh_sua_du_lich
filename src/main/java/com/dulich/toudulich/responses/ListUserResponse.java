package com.dulich.toudulich.responses;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListUserResponse {
    private List<UserResponse> userResponses ;
    private int totalPages ;
}
