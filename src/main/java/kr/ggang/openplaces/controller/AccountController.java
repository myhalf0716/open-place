package kr.ggang.openplaces.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ggang.openplaces.dao.entity.Member;
import kr.ggang.openplaces.model.JoinReq;
import kr.ggang.openplaces.model.TokenReq;
import kr.ggang.openplaces.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Member> join(@RequestBody JoinReq joinReq) {
        
        log.debug("JoinReq [{}]", joinReq);

        final HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<Member>(accountService.join(joinReq), headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/token",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody final TokenReq tokenReq) {
        log.debug("tokenReq [{}]", tokenReq);

        final HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<String>(accountService.login(tokenReq), headers, HttpStatus.CREATED);
    }

}
