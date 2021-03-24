package kr.ggang.openplaces.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Place implements Comparable<Place> {

    String name;
    EnumApiProvider provider;

    public Place(String name) {
        super();
        this.name = name;
    }

    public Place(String name, EnumApiProvider provider) {
        super();
        this.name = name;
        this.provider = provider;
    }

    /**
     * 공백을 제거한 name 으로 비교
     */
    @Override
    public int compareTo(Place o) {
        name.replaceAll("(^\\p{Z}+|\\p{Z}+$)", "").compareTo(o.name.replaceAll("(^\\p{Z}+|\\p{Z}+$)", ""));
        return 0;
    }
}
