package kr.ggang.openplaces.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Place {
    String name;

    public Place(String name) {
        super();
        this.name = name;
    }
}
