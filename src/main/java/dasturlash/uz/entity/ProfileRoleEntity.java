package dasturlash.uz.entity;

import dasturlash.uz.enums.Role;
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
    @Enumerated(EnumType.STRING)
    private Role role;

    public ProfileRoleEntity(Integer profileId, Role role) {
        this.profileId = profileId;
        this.role = role;
    }
}
