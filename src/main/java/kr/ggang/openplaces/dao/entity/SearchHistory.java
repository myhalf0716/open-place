package kr.ggang.openplaces.dao.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TBM_SEARCH_HISTORY")
@Proxy(lazy = false)
public class SearchHistory {
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String historyId;
    @Column(nullable = false, length = 80)
    private String memberId;
    @Column(nullable = false, length = 40)
    private String keyword;
    @Column
    private LocalDateTime createdAt;
}
