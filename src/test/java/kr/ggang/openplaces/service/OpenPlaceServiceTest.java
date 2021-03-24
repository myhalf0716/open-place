package kr.ggang.openplaces.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.ggang.openplaces.exception.ServiceException;
import kr.ggang.openplaces.model.Place;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class OpenPlaceServiceTest {
    @Autowired
    private PlaceService naverPlaceService;

    @Autowired
    private PlaceService kakaoPlaceService;

    @Autowired
    private PlaceService openPlaceService;
    
    String keyword;
    
    @BeforeEach
    void init() {
        keyword = "BHC치킨 분당";
        log.debug("keyword for search setup [{}]", keyword);
    }

    @Test
    void test() {
        assertNotNull(naverPlaceService);
        assertNotNull(kakaoPlaceService);
//        fail("Not yet implemented");
        
//        naverSearchTest();
//        kakaoSearchTest();
        
//        placeSortTest();
        openPlaceTest();
    }

    private void openPlaceTest() {
        List<Place> openPlaces = openPlaceService.search(keyword);
    }

    private void placeSortTest() {
        List<Place> kakaoPlaces = kakaoPlaceService.search(keyword);
        List<Place> naverPlaces = naverPlaceService.search(keyword);
        
        
        Map<String, Place> mergedPlacesMap = naverPlaces.stream()
                .collect(Collectors.toMap(Place::getName, Function.identity()));
        
        mergedPlacesMap.putAll(kakaoPlaces.stream()
                .collect(Collectors.toMap(Place::getName, Function.identity())));
        
        Comparator<Place> comparePlace = Comparator
                .comparing(Place::getProvider)
                .thenComparing(p->p.getName().replaceAll("(^\\p{Z}+|\\p{Z}+|\\p{Z}+$|<b>|</b>)", ""));
        
        List<Place> placesSorted = mergedPlacesMap.values().stream()
            .sorted(comparePlace).collect(Collectors.toList());


        log.debug("Kakao Place result[{}]", kakaoPlaces.size());
        kakaoPlaces.forEach( p -> {
            log.debug("Place [{}]", p);
        });
        log.debug("Naver Place result[{}]", naverPlaces.size());
        naverPlaces.forEach( p -> {
            log.debug("Place [{}]", p);
        });
        
        log.debug("Sorted Place result[{}]", placesSorted.size());
        placesSorted.forEach( p -> {
            log.debug("Place [{}]", p);
        });
        
    }

    private void kakaoSearchTest() {
        try {
            List<Place> result = kakaoPlaceService.search(keyword);
            assertNotNull(result);
            assertFalse(result.isEmpty());
        } catch(ServiceException e) {
            log.error("kakaoPlaceService.search error [{}|{}|{}]", e.getStatusCode(), e.getStatusText(), e.getResponseBodyAsString());
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

    }

    private void naverSearchTest() {
        try {
            log.debug("naverSearchTest keyword [{}]", keyword);
            List<Place> result = naverPlaceService.search(keyword);
            assertNotNull(result);
            assertFalse(result.isEmpty());
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

    }

}
