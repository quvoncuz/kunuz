package dasturlash.uz.entity;

import dasturlash.uz.enums.SmsStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "email")
@Getter
@Setter
@NoArgsConstructor
public class EmailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String code;

    @Column(name = "email", nullable = false)
    private String email;

    @Column
    private String body;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "sms_status")
    @Enumerated(EnumType.STRING)
    private SmsStatus smsStatus;

    public EmailEntity(String code, String email, String body, SmsStatus smsStatus) {
        this.code = code;
        this.email = email;
        this.body = body;
        this.smsStatus = smsStatus;
    }
}
