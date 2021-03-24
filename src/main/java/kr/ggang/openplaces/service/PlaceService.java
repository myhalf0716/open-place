package kr.ggang.openplaces.service;

import java.util.List;

import kr.ggang.openplaces.dao.entity.SearchHistory;
import kr.ggang.openplaces.model.Place;

public interface PlaceService {

    List<Place> search(String keyword);

    List<SearchHistory> searchHistory(String memberId, Integer size);

    List<String> searchHistoryHot();

}
