package kr.ggang.openplaces.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import kr.ggang.openplaces.exception.ServiceException;
import kr.ggang.openplaces.model.NaverPlaceResponse;
import kr.ggang.openplaces.model.Place;
import lombok.extern.slf4j.Slf4j;

/**
 * Client ID : cHpcZ7xP_InAHp6pxHxI ("${openapi.naver.client.id}")
 * Client Secret :4_6CiyF0Qg ("${openapi.naver.client.secret}")
 * 
 * @author seunghagkang
 *
 */
@Slf4j
@Service(value = "naverPlaceService")
public class NaverPlaceService implements PlaceService {
    private static final String NAVER_API_ENDPOINT = "https://openapi.naver.com";
    
    @Value("${openapi.naver.client.id}")
    private String clientId;
    
    @Value("${openapi.naver.client.secret}")
    private String clientSecret;
    
    @Autowired
    private RestTemplate restTemplate;


    @Override
    public List<Place> search(String keyword) {
        String apiUrl = "/v1/search/local.json";
        URI uri = UriComponentsBuilder.fromHttpUrl(NAVER_API_ENDPOINT.concat(apiUrl))
            .queryParam("query", keyword)
            .queryParam("sort", "random")
            .queryParam("page", 1)
            .queryParam("size", 10)
            .build().toUri()
            ;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(new MediaType[] {MediaType.APPLICATION_JSON}));
        
        headers.put("X-Naver-Client-Id", Arrays.asList(new String[] {clientId}) );
        headers.put("X-Naver-Client-Secret", Arrays.asList(new String[] {clientSecret}) );

        HttpEntity<?> entity = new HttpEntity<>(headers);

        NaverPlaceResponse naverPlace = null;
        try {
            ResponseEntity<NaverPlaceResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, NaverPlaceResponse.class);
            naverPlace = responseEntity.getBody();
            log.debug("naverPlace [{}]", naverPlace);
            
        } catch(Exception e) {
            log.error("fail to restTemplate.exchange====================================\n"
                    + "HEADER [{}] KEYWORD [{}]", headers, keyword, e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        
        if( naverPlace.getRss().getChannel().getTotal() == 0 ) {
            return new ArrayList<>();
        }
        
        List<Place> result = Arrays.asList(naverPlace.getRss().getChannel().getItem()).stream()
            .map(NaverPlaceResponse.Item::getTitle).map(Place::new)
            .collect(Collectors.toList())
            ;

        return result;
    }

}
