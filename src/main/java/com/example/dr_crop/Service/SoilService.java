package com.example.dr_crop.Service;

import com.example.dr_crop.Model.ConditionResult;
import com.example.dr_crop.Model.SoilRequest;
import com.example.dr_crop.Model.SoilResult;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class SoilService {
    private final RestTemplate restTemplate = new RestTemplate();

    public SoilResult getSoilResult(SoilRequest soilRequest){
        String url = "http://localhost:9091/api/recommend";

        SoilRequest body = soilRequest;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SoilRequest> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<SoilResult> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, SoilResult.class);
        return response.getBody();
    }
}
