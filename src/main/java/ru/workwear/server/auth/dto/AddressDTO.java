package ru.workwear.server.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDTO {
    private int index;
    private String country;
    private String locality;
    private String street;
    private String house;
    private String apartment;
}
