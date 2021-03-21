package kr.ggang.openplaces.service;

import kr.ggang.openplaces.dao.entity.Member;
import kr.ggang.openplaces.model.JoinReq;
import kr.ggang.openplaces.model.TokenReq;

public interface AccountService {
    public Member join(JoinReq joinReq);

    public String login(TokenReq tokenReq);
}
