package kr.ggang.openplaces.service;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import kr.ggang.openplaces.model.Place;

@Service(value = "openPlaceService")
public class OpenPlaceService implements PlaceService {
    private static final String KAKAO_API_ENDPOINT = "https://dapi.kakao.com";
    
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Place> search(String keyword) {
        String apiUrl = "/v2/local/search/keyword.json";
        URI uri = UriComponentsBuilder.fromHttpUrl(KAKAO_API_ENDPOINT.concat(apiUrl))
            .queryParam("query", keyword)
            .queryParam("sort", "accuracy")
            .queryParam("page", 1)
            .queryParam("size", 10)
            .build().toUri()
            ;
        
//        restTemplate.getForEntity(uri, null)
        
        return null;
    }

}
