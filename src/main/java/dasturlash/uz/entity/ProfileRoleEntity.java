package dasturlash.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "profile_role")
@NoArgsConstructor
public class ProfileRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "profile_id")
    private Integer profileId;

    @Column
    private String role;

    public ProfileRoleEntity(Integer profileId, String role) {
        this.profileId = profileId;
        this.role = role;
    }
}
