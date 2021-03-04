package pl.tabo.advapp.avertisement;

import lombok.Data;

@Data
public class AdvertisementRequest {

    private String picture1;

    private String picture2;

    private String picture3;

    private String picture4;

    private String advDescription;

    private AdvertisementType advType;

    private double latitude;

    private double longitude;
}
