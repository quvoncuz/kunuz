package dasturlash.uz.entity;

import dasturlash.uz.enums.SmsStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "sms")
@Getter
@Setter
public class SmsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String code;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "sms_status")
    @Enumerated(EnumType.STRING)
    private SmsStatus smsStatus;
}
