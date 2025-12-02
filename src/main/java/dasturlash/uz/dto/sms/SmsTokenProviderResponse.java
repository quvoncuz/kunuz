package dasturlash.uz.dto.sms;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SmsTokenProviderResponse {
    private String message;
    private Data data;
    private String token_type;
    private LocalDate createdDate;

    @Getter
    @Setter
    public static class Data {
        private String token;
    }
}
