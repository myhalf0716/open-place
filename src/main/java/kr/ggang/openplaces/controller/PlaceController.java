package kr.ggang.openplaces.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.ggang.openplaces.dao.entity.Member;
import kr.ggang.openplaces.model.JoinReq;
import kr.ggang.openplaces.model.Place;
import kr.ggang.openplaces.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/place")
public class PlaceController {
    @Autowired
    private PlaceService naverPlaceService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Place>> join(@RequestParam String keyword) {

        final HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<List<Place>>(naverPlaceService.search(keyword), headers,
                HttpStatus.OK);
    }

}
