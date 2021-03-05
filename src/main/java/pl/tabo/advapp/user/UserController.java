package pl.tabo.advapp.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:4200")
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/getuserdetails")
    public ResponseEntity<?> getUserDetails(){
        return userService.getUserDto();
    }


    @PutMapping("/changedetails")
    public ResponseEntity<?> changeDetails(@RequestBody UserDto userDto){
        return userService.changeDetails(userDto);
    }

    @PutMapping("/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        return userService.changePassword(changePasswordRequest);
    }
}
