package pl.tabo.advapp.avertisement;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/advertisement")
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    @GetMapping
    public List<Advertisement> getAll() {
        return advertisementService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDto> getById(@PathVariable Long id) {
        return advertisementService.getById(id);
    }

    @PostMapping("/searchbyrange")
    public List<AdvertisementDto> getByRange(@RequestBody SearchRequest searchRequest) {
        return advertisementService.getByRange(searchRequest);
    }

    @GetMapping("/home")
    public List<AdvertisementDto> allAdvertisement() {
        return advertisementService.getAllDto();
    }


    @PostMapping("/addnew")
    public Advertisement advertisement(@RequestBody AdvertisementRequest advertisementRequest) {

        return advertisementService.addAdverttisement(advertisementRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Advertisement> updateUser(@PathVariable Long id, @RequestBody Advertisement advertisement) {
        return advertisementService.updateAdvertisement(id, advertisement);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        advertisementService.dleteById(id);
    }

    @PostMapping("/getbyuser")
    public List<AdvertisementDto> getAllByUser(){
        return advertisementService.getAllByUser();
    }

    @PostMapping("/close")
    public ResponseEntity<?> setToUnAvtive(@RequestBody CloseRequest closeRequest){
        System.out.println(closeRequest);
        return advertisementService.setToUnActive(closeRequest);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody AdvertisementDto advertisementDto){
        return advertisementService.update(advertisementDto);
    }

}
