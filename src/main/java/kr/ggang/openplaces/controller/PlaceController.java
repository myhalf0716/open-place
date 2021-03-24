package kr.ggang.openplaces.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kr.ggang.openplaces.dao.entity.SearchHistory;
import kr.ggang.openplaces.model.Place;
import kr.ggang.openplaces.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PlaceController {
    @Autowired
    private PlaceService openPlaceService;

    @GetMapping(value = "/place",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Place> search(
            @RequestHeader(name = "X-AUTH-TOKEN") String token,
            @RequestParam String keyword) {

        return openPlaceService.search(keyword);
    }

    /**
     * query : size
     * @param token
     * @param keyword
     * @return
     */
    @GetMapping(value = "/history/member/{memberId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<SearchHistory> searchHistory(
            @PathVariable String memberId,
            @RequestParam Integer size) {

        return openPlaceService.searchHistory(memberId, size);
    }

    @GetMapping(value = "/history/hot",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> searchHistoryHot() {

        return openPlaceService.searchHistoryHot();
    }

}
