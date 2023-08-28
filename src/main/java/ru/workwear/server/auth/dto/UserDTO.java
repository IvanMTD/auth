package ru.workwear.server.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.workwear.server.auth.models.Gender;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private String confirmPassword;
    private String lastName;
    private String firstName;
    private String middleName;
    private String eMail;
    private String phoneNumber;
    private Gender gender;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthdate;
    private AddressDTO addressDTO;
    private String digitalSignature;
}
