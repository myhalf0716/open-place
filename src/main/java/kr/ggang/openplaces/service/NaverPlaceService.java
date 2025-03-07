package kr.ggang.openplaces.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.ggang.openplaces.dao.entity.SearchHistory;
import kr.ggang.openplaces.exception.ServiceException;
import kr.ggang.openplaces.model.EnumApiProvider;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Place> search(String keyword) {
        HttpURLConnection con = null;
        List<Place> placeList = null;
        String error = null;
        try {
            String apiUrl = NAVER_API_ENDPOINT.concat("/v1/search/local.json");
            String query = "?query="
                    .concat(URLEncoder.encode(keyword, "UTF-8"))
                    .concat("&sort=random&page=1&size=10");
//                    .concat("&sort=random");
            URL url = new URL(apiUrl + query);
            con = (HttpURLConnection) url.openConnection(); 
            con.setRequestMethod("GET"); 
            con.setRequestProperty("X-Naver-Client-Id", clientId); 
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            
        } catch( MalformedURLException e ) {
            log.error("fail to new URL", e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "fail to new URL");
        } catch(UnsupportedEncodingException e) {
            log.error("fail to URLEncoder.encode", e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "fail to URLEncoder.encode keyword");
        } catch (IOException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "fail to url.openConnection");
        }
        
        int responseCode;
        try {
            responseCode = con.getResponseCode();
        } catch (IOException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "fail to con.getResponseCode");
        }
        
        if( responseCode == HttpURLConnection.HTTP_OK ) {
            try {
                placeList = readNaverPlace(con.getInputStream());
            } catch (IOException e) {
                throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "fail to con.getInputStream");
            }
        }
        else {
            error = readError(con.getErrorStream());
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, error);
        }

        return placeList;
    }

    private String readError(InputStream errorStream) {
        // TODO Auto-generated method stub
        return null;
    }

    private List<Place> readNaverPlace(InputStream inputStream) {
        InputStreamReader streamReader = new InputStreamReader(inputStream); 
        String resJson = null;
        try (BufferedReader lineReader = new BufferedReader(streamReader)) { 
            StringBuilder responseBody = new StringBuilder(); 
            String line; 
            while ((line = lineReader.readLine()) != null) { 
                log.debug("readNaverPlace line [{}]", line);
                responseBody.append(line); 
            } 
            resJson = responseBody.toString(); 
            log.debug("readNaverPlace resJson [{}]", resJson);
        } catch (IOException e) { 
            log.error(e.getMessage(), e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "API 응답을 읽는데 실패했습니다."); 
        }

        NaverPlaceResponse naverPlace = null;
        try {
            naverPlace = objectMapper.readValue(resJson, NaverPlaceResponse.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "fail to objectMapper.readValue");
        }
        
        if( naverPlace.getTotal() == 0 ) {
            return new ArrayList<>();
        }
        
        List<Place> result = Arrays.asList(naverPlace.getItems()).stream()
            .map(item->{
                Place place = new Place(item.getTitle(), EnumApiProvider.NAVER);
                return place; })
            .collect(Collectors.toList())
            ;

        return result;
    }

//    @Override
    public List<Place> searchEx(String keyword) {
        String apiUrl = "/v1/search/local.json";
        URI uri = null;
        try {
            uri = UriComponentsBuilder.fromHttpUrl(NAVER_API_ENDPOINT.concat(apiUrl))
                .queryParam("query", URLEncoder.encode(keyword, "UTF-8"))
                .queryParam("sort", "random")
                .queryParam("page", 1)
                .queryParam("size", 10)
                .build().toUri();
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

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
        
        if( naverPlace.getTotal() == 0 ) {
            return new ArrayList<>();
        }
        
        List<Place> result = Arrays.asList(naverPlace.getItems()).stream()
            .map(NaverPlaceResponse.Item::getTitle).map(Place::new)
            .collect(Collectors.toList())
            ;

        return result;
    }

    @Override
    public List<SearchHistory> searchHistory(String memberId, Integer size) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> searchHistoryHot() {
        // TODO Auto-generated method stub
        return null;
    }

}
