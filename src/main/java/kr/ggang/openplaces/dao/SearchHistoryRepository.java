package kr.ggang.openplaces.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import kr.ggang.openplaces.dao.entity.SearchHistory;

public interface SearchHistoryRepository extends CrudRepository<SearchHistory, Long> {
    List<SearchHistory> findByMemberId(String memberId);
    List<SearchHistory> findAllByMemberId(String memberId, PageRequest pageRequest);
    
    @Query(value =
            "select g1.keyword from ( " +
                "select   keyword, count(keyword) cnt " +
                    "from tbm_search_history " +
                    "group by keyword ) g1 " +
            "order by g1.cnt desc " +
            "limit 10",
            nativeQuery = true
    )
    List<String> findHotKeyword();
    
}
