package com.technical.test.gli.rest;

import com.technical.test.gli.dto.DogApiResponse;
import com.technical.test.gli.dto.DogSubBreedRequest;
import com.technical.test.gli.dto.DogSubBreedResponse;
import com.technical.test.gli.exception.CustomResponseErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DogApiRest {

    Logger logger = LoggerFactory.getLogger(DogApiRest.class);

    @Autowired
    private Environment env;
    @Autowired
    private WebClient webClient;

    @Autowired
    private RestTemplate restTemplate;

    private void makeRestApiCall(String endpoint) {
        HttpComponentsClientHttpRequestFactory factory = (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
        int timeout = endpoint.equals(env.getProperty("dog.api.breed.list.url")) ? 5000 : 2000;
        factory.setReadTimeout(timeout);
        factory.setConnectTimeout(timeout);
        restTemplate.setRequestFactory(factory);
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
    }

    public DogApiResponse getBreedList() {
        String url = env.getProperty("dog.api.breed.list.url");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        makeRestApiCall(url);
        return restTemplate.exchange(url, HttpMethod.GET, entity, DogApiResponse.class).getBody();
    }

    public CompletableFuture<DogSubBreedResponse> getSubBreed(DogSubBreedRequest dogSubBreedRequest) {
        String url = env.getProperty("dog.api.sub.breed.url")
                .replace("{dogName}", dogSubBreedRequest.getDogName())
                .replace("{dogSubName}", dogSubBreedRequest.getDogSubName());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        makeRestApiCall(url);
        CompletableFuture<DogSubBreedResponse> response = CompletableFuture.supplyAsync(() -> {
            try {
                return restTemplate.exchange(url, HttpMethod.GET, entity, DogSubBreedResponse.class).getBody();
            } catch (Exception e) {
                logger.error("Error: ", e);
                throw new RuntimeException(e);
            }
        });
        return response;
    }

    public DogSubBreedResponse getBreed(DogSubBreedRequest dogSubBreedRequest) {
        String url = env.getProperty("dog.api.breed.url")
                .replace("{dogName}", dogSubBreedRequest.getDogName());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        makeRestApiCall(url);

        DogSubBreedResponse response = restTemplate.exchange(url, HttpMethod.GET, entity,
                DogSubBreedResponse.class).getBody();

        List<String> newDataList = response.getMessage().stream()
                .filter(a -> checkData(a))
                .collect(Collectors.toList());

        DogSubBreedResponse result = DogSubBreedResponse.builder()
                .status(response.getStatus())
                .message(newDataList)
                .build();
        return result;
    }

    private boolean checkData(String data) {
        boolean isValid = false;
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            int number = Integer.parseInt(matcher.group());
            if(number %2 != 0) {
                isValid = true;
            }
        }
        return isValid;
    }
}
