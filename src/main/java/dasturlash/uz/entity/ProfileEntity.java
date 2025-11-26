package dasturlash.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;
    @Column
    private String surname;
    @Column(unique = true)
    private String username;
    @Column
    private String password;
    @Column
    private String status;
    @Column(name = "visible")
    private Boolean visible = true;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;
    @Column(name = "photo_id")
    private Integer photoId;
}
