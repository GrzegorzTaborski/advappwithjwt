package pl.tabo.advapp.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
   private String currentPassword;
   private String changedPassword;
}
