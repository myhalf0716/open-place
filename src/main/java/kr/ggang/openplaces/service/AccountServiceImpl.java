package kr.ggang.openplaces.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import kr.ggang.openplaces.dao.MemberRepository;
import kr.ggang.openplaces.dao.entity.Member;
import kr.ggang.openplaces.exception.AccountException;
import kr.ggang.openplaces.model.JoinReq;
import kr.ggang.openplaces.model.TokenReq;
import kr.ggang.openplaces.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public Member join(JoinReq joinReq) {
        if( passwordEncoder == null ) {
            throw new AccountException(HttpStatus.INTERNAL_SERVER_ERROR, "password encoder not created");
        }
        
        if( !StringUtils.hasText(joinReq.getMemberId()) || !StringUtils.hasText(joinReq.getPassword()) ) {
            throw new AccountException(HttpStatus.BAD_REQUEST, 
                    String.format("invalid request data [%s]", joinReq.toString()) );
        }
        
        Member member = null;
        try {
            member = Member.builder()
            .memberId(joinReq.getMemberId())
            .name(joinReq.getName())
            .email(joinReq.getEmail())
            .password(passwordEncoder.encode(joinReq.getPassword()))
            .roles(Collections.singletonList("ROLE_MEMBER"))
            .build();
        } catch(Exception e) {
            log.error("fail to build member : {}", e.getMessage(), e);
            throw new AccountException(HttpStatus.BAD_REQUEST, "invalid request data");
        }
        
        return memberRepository.save(member);
    }

    @Override
    public String login(TokenReq tokenReq) {
        Member member = memberRepository.findByMemberId(tokenReq.getMemberId()).orElseThrow(
                ()->new AccountException(HttpStatus.NOT_FOUND, "Member not exists"));
        if( ! passwordEncoder.matches( tokenReq.getPassword(), member.getPassword()) ) {
            throw new AccountException(HttpStatus.UNAUTHORIZED, "invalid input");
        }
        return jwtUtil.createToken(String.valueOf(member.getMemberKey()), member.getRoles());
    }
    
}
