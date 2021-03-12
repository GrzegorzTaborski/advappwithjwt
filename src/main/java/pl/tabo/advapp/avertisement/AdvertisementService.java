package pl.tabo.advapp.avertisement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.tabo.advapp.exception.AdvertisementNotFoundException;
import pl.tabo.advapp.registration.MessageResponse;
import pl.tabo.advapp.user.User;
import pl.tabo.advapp.user.UserRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementDtoCon advertisementDtoCon;
    private final UserRepository userRepository;

    public List<Advertisement> getAll() {
        return advertisementRepository.findAll();
    }

    public List<AdvertisementDto> getAllDto() {
        return advertisementRepository.findAll().stream().filter(Advertisement::isActive).map(advertisementDtoCon::convertToDto).collect(Collectors.toList());
    }

    public ResponseEntity<AdvertisementDto> getById(Long id) {
        Advertisement adv = advertisementRepository.findById(id).orElseThrow(() -> new AdvertisementNotFoundException("Advertisement doesn't exist"));
        return ResponseEntity.ok(convertToDto(adv));
    }

    public Advertisement addAdverttisement(AdvertisementRequest advertisementRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<User> user = userRepository.findByEmail(userEmail);
        Advertisement advertisement = new Advertisement();
        advertisement.setActive(true);
        advertisement.setAdvDescription(advertisementRequest.getAdvDescription());
        advertisement.setAdvType(advertisementRequest.getAdvType());
        advertisement.setPicture1(advertisementRequest.getPicture1());
        advertisement.setPicture2(advertisementRequest.getPicture2());
        advertisement.setPicture3(advertisementRequest.getPicture3());
        advertisement.setPicture4(advertisementRequest.getPicture4());
        advertisement.setCreated(ZonedDateTime.now());
        advertisement.setLatitude(advertisementRequest.getLatitude());
        advertisement.setLongitude(advertisementRequest.getLongitude());
        advertisement.setUser(user.get());
        return advertisementRepository.save(advertisement);
    }

    public ResponseEntity<Advertisement> updateAdvertisement(Long id, Advertisement advertisementDetails) {
        Advertisement advertisement = advertisementRepository.findById(id).orElseThrow(() -> new AdvertisementNotFoundException("Advertisement doesn't exist"));
        advertisement.setActive(advertisementDetails.isActive());
        advertisement.setAdvDescription(advertisementDetails.getAdvDescription());
        advertisement.setAdvType(advertisementDetails.getAdvType());
        advertisement.setPicture1(advertisementDetails.getPicture1());
        advertisement.setPicture2(advertisementDetails.getPicture2());
        advertisement.setPicture3(advertisementDetails.getPicture3());
        advertisement.setPicture4(advertisementDetails.getPicture4());
        advertisementRepository.save(advertisement);
        return ResponseEntity.ok(advertisement);
    }

    public AdvertisementDto convertToDto(Advertisement advertisement) {
        return advertisementDtoCon.convertToDto(advertisement);
    }

    public List<AdvertisementDto> getByRange(SearchRequest searchRequest) {
        System.out.println(searchRequest);
        List<Advertisement> list = advertisementRepository.findAll();
        List<Advertisement> sorted = new ArrayList<>();
        if (searchRequest.getRange() >= 0) {
            for (Advertisement adv : list) {
                if (getRange(searchRequest.getUserLatitude(), searchRequest.getUserLongtitude(), adv.getLatitude(), adv.getLongitude()) < searchRequest.getRange())
                    sorted.add(adv);
            }
            if (searchRequest.getTypeSelected() != null) {
                return sorted.stream().map(a -> convertToDto(a)).filter(a -> a.getAdvertisementType().equals(searchRequest.getTypeSelected())).collect(Collectors.toList());
            } else {
                return sorted.stream().map(a -> convertToDto(a)).collect(Collectors.toList());
            }
        } else if (searchRequest.getTypeSelected() != null) {
            return this.getAllDto().stream().filter(a -> a.getAdvertisementType().equals(searchRequest.getTypeSelected())).collect(Collectors.toList());
        }
        return this.getAllDto();
    }

    private Double getRange(Double uLat, Double uLon, Double aLat, Double aLon) {
        final int earthRadius = 6371;
        Double lat1 = Double.valueOf(uLat);
        Double lon1 = Double.valueOf(uLon);
        Double lat2 = Double.valueOf(aLat);
        Double lon2 = Double.valueOf(aLon);
        Double latDistance = toRad(lat2 - lat1);
        Double lonDistance = toRad(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double distance = earthRadius * c;
        return distance;

    }

    private Double toRad(Double value) {
        return value * Math.PI / 180;
    }


    public void dleteById(Long id) {
        advertisementRepository.deleteById(id);
    }

    public List<AdvertisementDto> getAllByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<User> user = userRepository.findByEmail(userEmail);
        return advertisementRepository.findAllByUser(user.get()).stream().filter(Advertisement::isActive).map(this::convertToDto).collect(Collectors.toList());
    }

    public ResponseEntity<?> setToUnActive(CloseRequest closeRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            Optional<Advertisement> first = advertisementRepository.findAllByUser(user.get()).stream().filter(a -> a.getId().equals(closeRequest.getId())).findFirst();
            if (first.isPresent()) {
                first.get().setActive(false);
                advertisementRepository.save(first.get());
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse("Closed"));
            }
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Something goes wrong"));

    }

    public ResponseEntity<?> update(AdvertisementDto updatedAdvertisementDto) {
        Optional<Advertisement> adv = advertisementRepository.findById(updatedAdvertisementDto.getId());
        if(adv.isPresent()){
            adv.get().setPicture1(updatedAdvertisementDto.getPicture1());
            adv.get().setPicture2(updatedAdvertisementDto.getPicture2());
            adv.get().setPicture3(updatedAdvertisementDto.getPicture3());
            adv.get().setPicture4(updatedAdvertisementDto.getPicture4());
            adv.get().setAdvDescription(updatedAdvertisementDto.getDescription());
            adv.get().setAdvType(updatedAdvertisementDto.getAdvertisementType());
            advertisementRepository.save(adv.get());
             return ResponseEntity
                    .ok()
                    .body(new MessageResponse("Updated!"));
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Something goes wrong"));

    }
}
