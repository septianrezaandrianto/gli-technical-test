package com.technical.test.gli.service;

import com.google.gson.Gson;
import com.technical.test.gli.dto.DogApiResponse;
import com.technical.test.gli.dto.DogResponse;
import com.technical.test.gli.dto.DogSubBreedRequest;
import com.technical.test.gli.dto.DogSubBreedResponse;
import com.technical.test.gli.entity.Dog;
import com.technical.test.gli.repository.DogRepository;
import com.technical.test.gli.rest.DogApiRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class DogApiService {

    Logger logger = LoggerFactory.getLogger(DogApiService.class);

    @Autowired
    private DogApiRest dogApiRest;
    @Autowired
    private DogRepository dogRepository;

    public DogApiResponse getDogDataList() {
        Map<String, List<String>> dataList = dogApiRest.getBreedList().getMessage();

        Map<String, List<String>> newMap =  new TreeMap<>();
        dataList.forEach((key, value) -> {
            mappingData(key, value, newMap);
        });

        return new DogApiResponse(dogApiRest.getBreedList().getStatus(), newMap);
    }

    private Map<String, List<String>> mappingData(String key, List<String> value, Map<String, List<String>> newMap) {
        if (key.equals("sheepdog")) {
            value.forEach(v -> newMap.put(key + "-" + v, new ArrayList<>()));
        } else if (key.equals("terrier")) {
            value.forEach(v ->
            {
                try {
                    newMap.put(key + "-" + v,
                            getSubBreed(key, v).getMessage());
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        } else if(key.equals("shiba")) {
            newMap.put(key, dogApiRest.getBreed(new DogSubBreedRequest(key, null))
                    .getMessage());
        } else {
            newMap.put(key, value);
        }

        return newMap;
    }

    private DogSubBreedResponse getSubBreed(String name, String subName) throws ExecutionException, InterruptedException {
        DogSubBreedResponse data = dogApiRest.getSubBreed(new DogSubBreedRequest(name, subName)).get();

        List<String> dataList = data.getMessage();
        if(data.getMessage().size() > 4) {
            dataList = data.getMessage().subList(0,4);
        }

        List<String> newList = new ArrayList<>();

        dataList.stream().forEach(a -> {
            newList.add(a.replace("/", "\\/"));
        });

        return new DogSubBreedResponse(data.getStatus(), newList);
    }

    public DogResponse createDogData(DogSubBreedRequest request) {
        Map<String, List<String>> dogDataList = dogApiRest.getBreedList().getMessage();

        DogResponse result = new DogResponse();
        boolean isFound = false;
        for(String data : dogDataList.keySet()) {
            if (data.equalsIgnoreCase(request.getDogName())) {
                Dog existDog = dogRepository.findByDogName(request.getDogName());
                if(existDog != null) {
                    result.setResponseCode(HttpStatus.BAD_REQUEST.value());
                    result.setResponseMessage("Data already exist : " + request.getDogName());
                    return result;
                }
                Dog dog = Dog.builder()
                        .dogName(request.getDogName())
                        .value(new Gson().toJson(dogDataList.get(data)))
                        .build();
                dogRepository.save(dog);

                result.setResponseCode(HttpStatus.OK.value());
                result.setResponseMessage("Success create new data dog : " + request.getDogName());
                isFound = true;
                break;
            }
        }

        if(!isFound) {
            result.setResponseCode(HttpStatus.NOT_FOUND.value());
            result.setResponseMessage("Data not found");
        }

        return result;
    }

    public DogResponse getDogDataListFromDB() {
        List<Dog> dogList = dogRepository.findAll();

        DogResponse result = new DogResponse();
        if(!dogList.isEmpty()) {
            result.setResponseCode(HttpStatus.OK.value());
            result.setResponseMessage("Success get data from database");
            Map<String, List<String>> data = new HashMap<>();
            Map<String, List<String>> newData = new TreeMap<>();
            dogList.forEach(dog -> {
                data.put(dog.getDogName(), new Gson().fromJson(dog.getValue(), List.class));
                mappingData(dog.getDogName(), new Gson().fromJson(dog.getValue(), List.class), newData);
            });
            result.setData(newData);
        } else {
            result.setResponseCode(HttpStatus.NOT_FOUND.value());
            result.setResponseMessage("Data not found");
        }

        return result;
    }

}
