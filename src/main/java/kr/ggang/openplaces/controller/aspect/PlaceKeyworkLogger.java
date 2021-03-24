package kr.ggang.openplaces.controller.aspect;

import java.time.LocalDateTime;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import kr.ggang.openplaces.dao.SearchHistoryRepository;
import kr.ggang.openplaces.dao.entity.SearchHistory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class PlaceKeyworkLogger {
    @Value("${spring.security.jwt.secret}")
    private String secretKey;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;
    
    @AfterReturning(pointcut = "execution(* kr.ggang.openplaces.controller.PlaceController.search(..))"
            + "&& args(token, keyword)")
    public void hookClientControllerRequest(JoinPoint joinPoint, 
            String token,
            String keyword) throws Throwable {
        log.debug("hookClientControllerRequest ===========================================");
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        
        String memberId = userDetails.getUsername();
        SearchHistory sh = new SearchHistory();
        sh.setKeyword(keyword);
        sh.setMemberId(memberId);
        sh.setCreatedAt(LocalDateTime.now());
        
        searchHistoryRepository.save(sh);
    }
    
    private String getUserPk(String jwt) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody().getSubject();
    }

}
