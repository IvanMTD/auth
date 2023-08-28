package ru.workwear.server.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    private long id;

    private String username;
    @JsonIgnore
    private String password;
    private List<Authority> authorities;

    private String lastName;
    private String firstName;
    private String middleName;

    private String eMail;
    private String phoneNumber;

    private Gender gender;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthdate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate registrationDate;
    @Transient
    private Address address;
    private Long addressId;
    @Transient
    private String accessToken;

    public void setAddress(Address address){
        this.addressId = address.getId();
        this.address = address;
    }

    public void addAuthority(Authority authority){
        if(this.authorities == null){
            authorities = new ArrayList<>();
        }
        this.authorities.add(authority);
    }

    public enum Authority {
        AUTHORITY_Create,
        AUTHORITY_Read,
        AUTHORITY_Update,
        AUTHORITY_Delete
    }
}
