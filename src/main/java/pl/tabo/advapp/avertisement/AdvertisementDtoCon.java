package pl.tabo.advapp.avertisement;

import org.springframework.stereotype.Component;
import pl.tabo.advapp.user.User;

@Component
public class AdvertisementDtoCon {
    public AdvertisementDto convertToDto(Advertisement advertisement) {

        AdvertisementDto advertisementDto = new AdvertisementDto();
        User user = advertisement.getUser();
        advertisementDto.setId(advertisement.getId());
        advertisementDto.setAdvertisementType(advertisement.getAdvType());
        advertisementDto.setPicture1(advertisement.getPicture1());
        advertisementDto.setPicture2(advertisement.getPicture2());
        advertisementDto.setPicture3(advertisement.getPicture3());
        advertisementDto.setPicture4(advertisement.getPicture4());
        advertisementDto.setDescription(advertisement.getAdvDescription());
        advertisementDto.setCreated(advertisement.getCreated());
        advertisementDto.setUserNameAndLastName(user.getInfo());
        advertisementDto.setLatitude(advertisement.getLatitude());
        advertisementDto.setLongitude(advertisement.getLongitude());


        return advertisementDto;


    }
}
