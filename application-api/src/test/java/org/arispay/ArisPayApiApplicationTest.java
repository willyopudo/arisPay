package org.arispay;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class ArisPayApiApplicationTest {
    @Test
    void contextLoads() {
        int myInt = Integer.parseInt("123");
        assertEquals (myInt, 123);
    }
}
