package kr.ggang.openplaces.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TrimTest {

    @BeforeEach
    void setUp() throws Exception {}

    @Test
    void test() {
        String name = "<b>정자동</b>행정 복지 센터";
        String exp = "(^\\p{Z}+|\\p{Z}+|\\p{Z}+$|<b>|</b>)";
        String expected = "<b>정자동</b>행정복지센터";
        String trimmed = name.replaceAll(exp, "");
        
        log.debug("name [{}] trimmed [{}]", name, trimmed);
        assertTrue(expected.equals(trimmed));
        
        
//        fail("Not yet implemented");
        
    }

}
