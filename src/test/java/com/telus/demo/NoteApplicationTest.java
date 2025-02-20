package com.telus.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NoteApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void testMainMethod() {
        NoteApplication.main(new String[]{});
    }

}
