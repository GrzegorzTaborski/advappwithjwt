package pl.tabo.advapp.avertisement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementDto {
    private Long id;

    private AdvertisementType advertisementType;

    private String picture1;

    private String picture2;

    private String picture3;

    private String picture4;

    private String description;

    private ZonedDateTime created;

    private String userNameAndLastName;

    private double latitude;

    private double longitude;

}
