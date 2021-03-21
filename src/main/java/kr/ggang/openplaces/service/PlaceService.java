package kr.ggang.openplaces.service;

import java.util.List;

import kr.ggang.openplaces.model.Place;

public interface PlaceService {

    List<Place> search(String keyword);

}
