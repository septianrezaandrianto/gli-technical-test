package com.technical.test.gli.service;

import com.google.gson.Gson;
import com.technical.test.gli.dto.DogApiResponse;
import com.technical.test.gli.dto.DogResponse;
import com.technical.test.gli.dto.DogSubBreedRequest;
import com.technical.test.gli.dto.DogSubBreedResponse;
import com.technical.test.gli.entity.Dog;
import com.technical.test.gli.exception.BadRequestException;
import com.technical.test.gli.exception.ConflictException;
import com.technical.test.gli.exception.NotFoundException;
import com.technical.test.gli.repository.DogRepository;
import com.technical.test.gli.rest.DogApiRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class DogApiService {

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
            newMap.put(key, dogApiRest.getBreed(new DogSubBreedRequest(key, null, "shiba"))
                    .getMessage());
        } else {
            newMap.put(key, value);
        }

        return newMap;
    }

    public DogSubBreedResponse getSubBreed(String name, String subName) throws ExecutionException, InterruptedException {
        DogSubBreedResponse data = dogApiRest.getSubBreed(new DogSubBreedRequest(name, subName, null)).get();

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
                    throw new ConflictException("Data already exist : " + request.getDogName());
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
            throw new NotFoundException("Data not found : " + request.getDogName());
        }

        return result;
    }

    public DogResponse getDogDataListFromDB() {
        List<Dog> dogList = dogRepository.findAll();

        DogResponse result = new DogResponse();
        if(dogList.isEmpty()) {
            throw new NotFoundException("Data not found");
        }

        result.setResponseCode(HttpStatus.OK.value());
        result.setResponseMessage("Success get data list from database");
        Map<String, List<String>> data = new HashMap<>();
        Map<String, List<String>> newData = new TreeMap<>();
        dogList.forEach(dog -> {
            data.put(dog.getDogName(), new Gson().fromJson(dog.getValue(), List.class));
            mappingData(dog.getDogName(), new Gson().fromJson(dog.getValue(), List.class), newData);
        });
        result.setData(newData);
        return result;
    }


    public DogResponse getDogDataByName(String dogName) {
        DogResponse result = new DogResponse();

        Dog dog = dogRepository.findByDogName(dogName);
        if(dog == null) {
            throw new NotFoundException("Data not found : " + dogName);
        }

        result.setResponseCode(HttpStatus.OK.value());
        result.setResponseMessage("Success get data from database");
        Map<String, List<String>> data = new HashMap<>();
        Map<String, List<String>> newData = new TreeMap<>();
        data.put(dog.getDogName(), new Gson().fromJson(dog.getValue(), List.class));
        mappingData(dog.getDogName(), new Gson().fromJson(dog.getValue(), List.class), newData);
        result.setData(newData);

        return result;
    }


    public DogResponse deleteDogDataByName(String dogName) {
        DogResponse result = new DogResponse();

        Dog dog = dogRepository.findByDogName(dogName);
        if(dog == null) {
            throw new NotFoundException("Data not found : " + dogName);
        }

        dogRepository.delete(dog);
        result.setResponseCode(HttpStatus.OK.value());
        result.setResponseMessage("Success delete data from database " + dogName);
        return result;
    }

    public DogResponse updateDogImage(String oldDogName, String newDogName) throws ExecutionException, InterruptedException {
        DogResponse result = new DogResponse();

        Dog dog = dogRepository.findByDogName(oldDogName);
        if(dog == null) {
            throw new NotFoundException("Data not found oldDogName: " + oldDogName);
        } else if(ObjectUtils.isEmpty(newDogName.trim())) {
            throw new BadRequestException("Invalid newDogName: " + newDogName);
        }

        boolean isValid = false;
        Map<String, List<String>> dogDataList = dogApiRest.getBreedList().getMessage();
        for(String data : dogDataList.keySet()) {
            if (data.equalsIgnoreCase(newDogName)) {
                Dog existDog = dogRepository.findByDogName(newDogName);
                if(existDog != null) {
                    throw new ConflictException("Data already exist : " + newDogName);
                }

                Dog dogUpdate = Dog.builder()
                        .id(dog.getId())
                        .dogName(newDogName)
                        .value(new Gson().toJson(dogDataList.get(data)))
                        .build();
                dogRepository.save(dogUpdate);
                isValid = true;
                break;
            }
        }

        if(!isValid) {
            throw new NotFoundException("Data not found newDogName: " + newDogName);
        }
        result.setResponseCode(HttpStatus.OK.value());
        result.setResponseMessage("Success update image data from " + oldDogName + " to " + newDogName);
        return result;
    }
}
