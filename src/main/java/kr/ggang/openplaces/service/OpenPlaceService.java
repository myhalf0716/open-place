package kr.ggang.openplaces.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.ggang.openplaces.model.Place;

@Service
public class OpenPlaceService implements PlaceService {

    @Override
    public List<Place> search(String keyword) {
        return null;
    }

}
