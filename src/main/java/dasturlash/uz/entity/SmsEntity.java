package dasturlash.uz.entity;

import dasturlash.uz.enums.SmsStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "sms")
@Getter
@Setter
@NoArgsConstructor
public class SmsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String code;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column
    private String body;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "sms_status")
    @Enumerated(EnumType.STRING)
    private SmsStatus smsStatus;

    public SmsEntity(String phone, String code, String body) {
        this.phone = phone;
        this.code = code;
        this.body = body;
    }
}
