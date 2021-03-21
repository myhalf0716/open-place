package kr.ggang.openplaces.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinReq {
    private String memberId;
    private String password;
    private String name;
    private String email;
    
}
