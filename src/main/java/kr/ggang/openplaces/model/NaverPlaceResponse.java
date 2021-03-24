package kr.ggang.openplaces.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NaverPlaceResponse {
    /**
     *      <title>조선옥</title>
            <link />
            <category>한식&gt;육류,고기요리</category>
            <description>연탄불 한우갈비 전문점.</description>
            <telephone></telephone>
            <address>서울특별시 중구 을지로3가 229-1 </address>
            <roadAddress>서울특별시 중구 을지로15길 6-5 </roadAddress>
            <mapx>311277</mapx>
            <mapy>552097</mapy>
     * @author seunghagkang
     *
     */
    @Getter
    @Setter
    @ToString
    public static class Item {
        private String title;
        private String link;
        private String category;
        private String description;
        private String telephone;
        private String address;
        private String roadAddress;
        private Long mapx;
        private Long mapy;
    }

    /**
     *  <link>http://search.naver.com</link>
        <description>Naver Search Result</description>
        <lastBuildDate>Tue, 04 Oct 2016 13:10:58 +0900</lastBuildDate>
        <total>407</total>
        <start>1</start>
        <display>10</display>
        <item>

     * @author seunghagkang
     *
     */
    @Getter
    @Setter
    @ToString
    public static class Channel {
        private String title;
        private String link;
        private String description;
//        @JsonIgnore
//        private OffsetDateTime lastBuildDate;
        private Integer total;
        private Integer start;
        private Integer display;
        private Item[] item;
    }

    @Getter
    @Setter
    public static class RSS {
        private Channel channel;
    }
    
    private String lastBuildDate;
    private Integer total;
    private Integer start;
    private Integer display;
    private Item[] items;
}
