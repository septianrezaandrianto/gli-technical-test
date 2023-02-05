package com.technical.test.gli.controller;

import com.google.gson.Gson;
import com.technical.test.gli.dto.DogResponse;
import com.technical.test.gli.dto.DogSubBreedRequest;
import com.technical.test.gli.service.DogApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@WebMvcTest(DogApiController.class)
public class DogApiControllerTest {

    @MockBean
    private DogApiService dogService;
    @Autowired
    private MockMvc mockMvc;

    DogSubBreedRequest dogSubBreedRequest;
    @BeforeEach
    public void setUp() {
        dogSubBreedRequest = DogSubBreedRequest.builder()
                .dogName("terrier")
                .dogSubName("american")
                .build();
    }

    @Test
    @DisplayName("should return success createDogData_success")
    public void createDogData_success() throws Exception {
        when(dogService.createDogData(dogSubBreedRequest))
                .thenReturn(dogResponse(HttpStatus.OK.value(),
                        "Success create new data dog : " + dogSubBreedRequest.getDogName()));

        MockHttpServletResponse result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/createDogData")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(new Gson().toJson(dogSubBreedRequest)))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), result.getStatus());
    }

    @Test
    @DisplayName("should return success getDogDataListFromDB_success")
    public void getDogDataListFromDB_success() throws Exception {
        when(dogService.getDogDataListFromDB())
                .thenReturn(dogResponse(HttpStatus.OK.value(),
                        "Success get all data dog from DB"));

        MockHttpServletResponse result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/getDogDataListFromDb")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), result.getStatus());
    }

    @Test
    @DisplayName("should return success getDogDataByName_success")
    public void getDogDataByName_success() throws Exception {
        when(dogService.getDogDataByName(dogSubBreedRequest.getDogName()))
                .thenReturn(dogResponse(HttpStatus.OK.value(),
                        "Success get data from database"));

        MockHttpServletResponse result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/getDogDataByName")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("dogName", dogSubBreedRequest.getDogName())
                        .characterEncoding("utf-8"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), result.getStatus());
    }

    @Test
    @DisplayName("should return success deleteDogDataByName_success")
    public void deleteDogDataByName_success() throws Exception {
        when(dogService.deleteDogDataByName(dogSubBreedRequest.getDogName()))
                .thenReturn(dogResponse(HttpStatus.OK.value(),
                        "Success delete data from database"));

        MockHttpServletResponse result = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/deleteDogDataByName")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("dogName", dogSubBreedRequest.getDogName())
                        .characterEncoding("utf-8"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), result.getStatus());
    }

    @Test
    @DisplayName("should return success updateDogImage_success")
    public void updateDogImage_success() throws Exception {
        when(dogService.updateDogImage("terrier", "shiba"))
                .thenReturn(dogResponse(HttpStatus.OK.value(),
                        "Success update image dog : " + dogSubBreedRequest.getDogName()));

        MockHttpServletResponse result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/updateDogImage")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .param("oldDogName", "terrier")
                        .param("newDogName", "shiba"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), result.getStatus());
    }

    private DogResponse dogResponse(int responseCode, String responseMessage) {
        return DogResponse.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .build();
    }
}

