package kr.ggang.openplaces.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import kr.ggang.openplaces.dao.SearchHistoryRepository;
import kr.ggang.openplaces.dao.entity.SearchHistory;
import kr.ggang.openplaces.model.Place;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "openPlaceService")
public class OpenPlaceService implements PlaceService {
    @Autowired
    private PlaceService naverPlaceService;

    @Autowired
    private PlaceService kakaoPlaceService;
    
    @Autowired
    private SearchHistoryRepository searchHistoryRepository;


    @Override
    public List<Place> search(String keyword) {
        List<Place> kakaoPlaces = kakaoPlaceService.search(keyword);
        List<Place> naverPlaces = naverPlaceService.search(keyword);
        
        
        Map<String, Place> mergedPlacesMap = naverPlaces.stream()
                .collect(Collectors.toMap(p->p.getName().replaceAll("(^\\p{Z}+|\\p{Z}+|\\p{Z}+$|<b>|</b>)", ""), Function.identity()));
        
        /*
         * 중복 제거
         *      네이버 결과와 카카오 결과에 장소명 기준 중복 결과가 있을 경우
         *      Map 의 특성에 따라 나중에 등록된 장소만 객체만 남는다.
         *      카카오를 기준의 중복제거가 필요하므로, 카카오를 나중에 등록한다.
         */
        mergedPlacesMap.putAll(kakaoPlaces.stream()
                .collect(Collectors.toMap(p->p.getName().replaceAll("(^\\p{Z}+|\\p{Z}+|\\p{Z}+$|<b>|</b>)", ""), Function.identity())));
        
        mergedPlacesMap.keySet().forEach(k->log.debug("KEY [{}]", k));
        /*
         * provider, name 순으로 정열
         */
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
        
        return placesSorted;
    }


    @Override
    public List<SearchHistory> searchHistory(String memberId, Integer size) {
        if( size == null || size == 0 ) {
            size = 100;
        }
        
//        PageRequest pageRequest = PageRequest.of(0, size );
        List<SearchHistory> list = searchHistoryRepository.findByMemberId(memberId);
        Integer result = list.size();
        if( result > size ) {
            result = size;
        }
        return list.subList(0, result);
    }


    @Override
    public List<String> searchHistoryHot() {
        List<String> result = searchHistoryRepository.findHotKeyword();
        return result;
    }

}
