package kr.ggang.openplaces.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.ggang.openplaces.dao.entity.SearchHistory;
import kr.ggang.openplaces.exception.ServiceException;
import kr.ggang.openplaces.model.EnumApiProvider;
import kr.ggang.openplaces.model.KakaoPlaceResponse;
import kr.ggang.openplaces.model.Place;
import lombok.extern.slf4j.Slf4j;

/**
 * KakaoAK : a4b8cab008561bfc33ec2b072c8ea1ea
 * @author seunghagkang
 *
 */
@Slf4j
@Service(value = "kakaoPlaceService")
public class KakaoPlaceService implements PlaceService {
    private static final String KAKAO_API_ENDPOINT = "https://dapi.kakao.com";
    
    @Value("${openapi.kakao.admin.key}")
    private String adminKey;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Place> search(String keyword) {
        HttpURLConnection con = null;
        List<Place> placeList = null;
        String error = null;
        try {
            String apiUrl = KAKAO_API_ENDPOINT.concat("/v2/local/search/keyword.json");
            String query = "?query="
                    .concat(URLEncoder.encode(keyword, "UTF-8"))
                    .concat("&size=10");
//                    .concat("&sort=random");
            URL url = new URL(apiUrl + query);
            con = (HttpURLConnection) url.openConnection(); 
            con.setRequestMethod("GET"); 
            con.setRequestProperty("Authorization", "KakaoAK ".concat(adminKey)); 
            
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
                placeList = readKakaoPlace(con.getInputStream());
            } catch (IOException e) {
                throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "fail to con.getInputStream");
            }
        }
        else {
            error = readError(con.getErrorStream());
            log.error("readKakaoPlace Error[{}]{}]", responseCode, error);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, error);
        }

        return placeList;
    }

    private String readError(InputStream errorStream) {
        InputStreamReader streamReader = new InputStreamReader(errorStream); 
        String result = null;
        try (BufferedReader lineReader = new BufferedReader(streamReader)) { 
            StringBuilder responseBody = new StringBuilder(); 
            String line; 
            while ((line = lineReader.readLine()) != null) { 
                log.debug("readError line [{}]", line);
                responseBody.append(line); 
            } 
            result = responseBody.toString(); 
            log.debug("readError resJson [{}]", result);
        } catch (IOException e) { 
            log.error(e.getMessage(), e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "API 응답을 읽는데 실패했습니다."); 
        }
        
        return result;
    }

    private List<Place> readKakaoPlace(InputStream inputStream) {
        InputStreamReader streamReader = new InputStreamReader(inputStream); 
        String resJson = null;
        try (BufferedReader lineReader = new BufferedReader(streamReader)) { 
            StringBuilder responseBody = new StringBuilder(); 
            String line; 
            while ((line = lineReader.readLine()) != null) { 
                log.debug("readKakaoPlace line [{}]", line);
                responseBody.append(line); 
            } 
            resJson = responseBody.toString(); 
            log.debug("readKakaoPlace resJson [{}]", resJson);
        } catch (IOException e) { 
            log.error(e.getMessage(), e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "API 응답을 읽는데 실패했습니다."); 
        }

        KakaoPlaceResponse kakaoPlace = null;
        try {
            kakaoPlace = objectMapper.readValue(resJson, KakaoPlaceResponse.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "fail to objectMapper.readValue");
        }
        
        if( kakaoPlace.getMeta().getTotalCount() == 0 ) {
            return new ArrayList<>();
        }
        
        log.debug("documens length [{}]", kakaoPlace.getDocuments().length);
        List<Place> result = Arrays.asList(kakaoPlace.getDocuments()).stream()
            .map(item->{
                Place place = new Place(item.getPlaceName(), EnumApiProvider.KAKAO);
                return place; })
            .collect(Collectors.toList())
            ;
        for( Place place : result ) {
            log.debug("Place [{}]", place);
        }

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
