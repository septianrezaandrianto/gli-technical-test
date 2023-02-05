package com.technical.test.gli.service;

import com.google.gson.Gson;
import com.technical.test.gli.dto.DogApiResponse;
import com.technical.test.gli.dto.DogResponse;
import com.technical.test.gli.dto.DogSubBreedRequest;
import com.technical.test.gli.dto.DogSubBreedResponse;
import com.technical.test.gli.entity.Dog;
import com.technical.test.gli.exception.NotFoundException;
import com.technical.test.gli.repository.DogRepository;
import com.technical.test.gli.rest.DogApiRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DogApiServiceTest {

    @Mock
    private DogApiRest dogApiRest;
    @Mock
    private DogRepository dogRepository;
    @InjectMocks
    private DogApiService dogApiService;

    DogSubBreedRequest dogSubBreedRequest;
    @BeforeEach
    public void setUp() {
        dogSubBreedRequest = DogSubBreedRequest.builder()
                .dogName("terrier")
                .dogSubName("american")
                .build();
    }

    @Test
    @DisplayName("should return success deleteDogDataByName_success")
    public void deleteDogDataByName_success() {
        when(dogRepository.findByDogName(anyString())).thenReturn(dog("terrier", "american"));
        DogResponse result = dogApiService.deleteDogDataByName("terrier");
        assertEquals(dogResponse(HttpStatus.OK.value(), "Success delete data from database terrier",
                "crud"), result);
        verify(dogRepository, times(1)).delete(dog("terrier", "american"));
    }

    @Test
    @DisplayName("should return success getDogDataByName_notFound")
    public void getDogDataByName_notFound() {
        when(dogRepository.findByDogName(anyString())).thenReturn(null);
        NotFoundException result = assertThrows(NotFoundException.class, () -> {
            dogApiService.getDogDataByName("terrier");
        });

        assertEquals(notFoundException(), result);
    }

    @Test
    @DisplayName("should return success getDogDataListFromDB_notFound")
    public void getDogDataListFromDB_notFound() {
        when(dogRepository.findAll()).thenReturn(new ArrayList<>());
        NotFoundException result = assertThrows(NotFoundException.class, () -> {
            dogApiService.getDogDataListFromDB();
        });

        assertEquals(notFoundException(), result);
    }

    @Test
    @DisplayName("should return success deleteDogDataByName_notFound")
    public void deleteDogDataByName_notFound() {
        when(dogRepository.findByDogName(anyString())).thenReturn(null);
        NotFoundException result = assertThrows(NotFoundException.class, () -> {
            dogApiService.deleteDogDataByName("terrier");
        });

        assertEquals(notFoundException(), result);
    }

    private NotFoundException notFoundException() {
        return NotFoundException.builder()
                .responseCode(null)
                .responseMessage(null)
                .build();
    }
    private Dog dog(String dogName, String value) {
        return Dog.builder()
                .dogName(dogName)
                .value(value)
                .build();
    }
    private DogResponse dogResponse(int responseCode, String responseMessage, String flag) {
        if (flag.equals("crud")) {
            return DogResponse.builder()
                    .responseCode(responseCode)
                    .responseMessage(responseMessage)
                    .build();
        } else {
            return DogResponse.builder()
                    .responseCode(responseCode)
                    .responseMessage(responseMessage)
                    .data(mappingData())
                    .build();
        }
    }

    private DogSubBreedResponse dogSubBreedResponse() {
        return DogSubBreedResponse.builder()
                .status("success")
                .message(List.of("bobtail", "english", "shetland"))
                .build();
    }

    private Map<String, List<String>> mappingData() {
        Map<String, List<String>> newMap = new HashMap<>();
        newMap.put("sheepdog", List.of("bobtail", "english", "shetland"));
        newMap.put("terrier", List.of("american", "australian", "bedlington", "border", "dandie", "fox", "irish", "kerryblue", "lakeland", "norfolk", "norwich", "patterdale", "russell", "scottish", "sealyham", "silky", "tibetan", "toy", "westhighland", "wheaten", "yorkshire"));
        newMap.put("shiba", List.of("shiba"));
        return newMap;
    }

}
