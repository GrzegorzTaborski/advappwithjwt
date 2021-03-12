package pl.tabo.advapp.avertisement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.tabo.advapp.user.User;

import javax.persistence.*;
import java.time.ZonedDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    @Lob
    private String picture1;
    @Lob
    private String picture2;
    @Lob
    private String picture3;
    @Lob
    private String picture4;

    private String advDescription;

    @Enumerated(EnumType.STRING)
    private AdvertisementType advType;

    private boolean isActive;

    private ZonedDateTime created;

    private double latitude;

    private double longitude;


}
