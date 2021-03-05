package pl.tabo.advapp.avertisement;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SearchRequest {
    private double range;
    private AdvertisementType typeSelected;
    private double userLatitude;
    private double userLongtitude;
}
