package com.technical.test.gli.controller;

import com.technical.test.gli.dto.DogApiResponse;
import com.technical.test.gli.dto.DogResponse;
import com.technical.test.gli.dto.DogSubBreedRequest;
import com.technical.test.gli.service.DogApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api")
public class DogApiController {
    @Autowired
    private DogApiService dogApiService;

    @GetMapping("/getDogDataList")
    public ResponseEntity<DogApiResponse> getDogDataList() {
        return ResponseEntity.ok(dogApiService.getDogDataList());
    }

    @PostMapping("/createDogData")
    public ResponseEntity<DogResponse> createDogData(@RequestBody DogSubBreedRequest request) {
        return ResponseEntity.ok(dogApiService.createDogData(request));
    }

    @GetMapping("/getDogDataListFromDb")
    public ResponseEntity<DogResponse> getDogDataListFromDB() {
        return ResponseEntity.ok(dogApiService.getDogDataListFromDB());
    }
}
