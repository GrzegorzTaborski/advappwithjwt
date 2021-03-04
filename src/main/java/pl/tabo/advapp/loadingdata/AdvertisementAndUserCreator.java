package pl.tabo.advapp.loadingdata;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.tabo.advapp.avertisement.AdvertisementRepository;
import pl.tabo.advapp.user.UserRepository;

@Component
@AllArgsConstructor
public class AdvertisementAndUserCreator implements CommandLineRunner {
    private UserRepository userRepository;
    private AdvertisementRepository advertisementRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(String... args) throws Exception {

//        User user = new User();
//        user.setMobileNumber("12345");
//        user.setEmail("malpa@gmail.com");
//        user.setEnabled(true);
//        user.setFirstName("Jan");
//        user.setLastName("Kowalski");
//        user.setLocked(false);
//        user.setUsername("tabo");
//        user.setPassword(bCryptPasswordEncoder.encode("12345"));
//        System.out.println(Role.USER.toString());
//        userRepository.save(user);
//
//        BufferedImage img = ImageIO.read(new File("C:\\Spring\\App\\src\\main\\java\\pl\\tabo\\app\\loadingdata\\unnamed.jpg"));
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ImageIO.write(img, "jpg", bos );
//        byte [] data = bos.toByteArray();
//        Advertisement adv = new Advertisement();
//        adv.setAdvDescription("asdad");
//        adv.setActive(true);
//        adv.setAdvType(AdvertisementType.ELECTRONICS);
//        adv.setUser(user);
//        adv.setCreated(ZonedDateTime.now());
//        adv.setPicture1(data);
//        adv.setPicture2(data);
//        adv.setPicture3(data);
//        adv.setPicture4(data);
//        adv.setLongitude(21.0117800);
//        adv.setLatitude(52.2297700);
//        advertisementRepository.save(adv);
//        52.2297700,21.0117800;

    }
}