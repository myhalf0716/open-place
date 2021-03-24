package kr.ggang.openplaces.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoPlaceResponse {
    
    @Getter
    @Setter
    @ToString
    public static class Document {
        @JsonProperty("place_name")
        private String placeName;
        private String distance;
        @JsonProperty("place_url")
        private String placeUrl;
        @JsonProperty("category_name")
        private String categoryName;
        @JsonProperty("address_name")
        private String addressName;
        @JsonProperty("road_address_name")
        private String roadAddressName;
        private String id;
        private String phone;
        @JsonProperty("category_group_code")
        private String categoryGroupCode;
        @JsonProperty("category_group_name")
        private String categoryGroupName;
        private String x;
        private String y;
    }
    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiMeta {
        @JsonProperty("pageable_count")
        private Integer pageableCount;
        @JsonProperty("total_count")
        private Integer totalCount;
        @JsonProperty("is_end")
        private Boolean isEnd;
    }

    private ApiMeta meta;
    private Document[] documents;

}
