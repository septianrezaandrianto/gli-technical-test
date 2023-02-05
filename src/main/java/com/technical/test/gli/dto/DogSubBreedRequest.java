package com.technical.test.gli.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DogSubBreedRequest {
    private String dogName;
    private String dogSubName;
    private String flag;

}
