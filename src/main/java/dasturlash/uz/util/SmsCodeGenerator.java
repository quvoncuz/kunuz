package dasturlash.uz.util;

import java.util.Random;

public interface SmsCodeGenerator {
    default String generateSmsCode() {
        Random random = new Random();
        return random.nextInt(10000, 99999) + "";
    }
}
