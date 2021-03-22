package kr.ggang.openplaces.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import kr.ggang.openplaces.model.Place;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NaverPlaceServiceTest {
    @Autowired
    private PlaceService naverPlaceService;

    @Test
    void test() {
        assertNotNull(naverPlaceService);
//        fail("Not yet implemented");
        
        searchTest();
    }

    private void searchTest() {
        try {
            List<Place> result = naverPlaceService.search("정자동 초밥");
            assertNotNull(result);
            assertFalse(result.isEmpty());
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

    }

}
