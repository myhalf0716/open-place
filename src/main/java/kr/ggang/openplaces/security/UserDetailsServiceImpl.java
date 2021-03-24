package kr.ggang.openplaces.security;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import kr.ggang.openplaces.dao.MemberRepository;
import kr.ggang.openplaces.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Transactional
    public UserDetails loadUserByUsername(String userPk) {
        UserDetails ud = memberRepository.findById(Long.valueOf(userPk)).orElseThrow(MemberNotFoundException::new);
        ud.getAuthorities();
        return ud;
    }
}
